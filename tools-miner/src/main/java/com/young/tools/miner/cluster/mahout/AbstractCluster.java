/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.young.tools.miner.cluster.mahout;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import org.apache.hadoop.conf.Configuration;
import org.apache.mahout.common.parameters.Parameter;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.math.function.Functions;
import org.apache.mahout.math.function.SquareRootFunction;

public abstract class AbstractCluster implements Cluster {
  
  // cluster persistent state
  private int id;
  
  private long numPoints;
  
  private Vector center;
  
  private Vector radius;
  
  protected AbstractCluster() {}
  
  protected AbstractCluster(Vector point, int id2) {
    this.setNumPoints(0);
    this.setCenter(new RandomAccessSparseVector(point));
    this.setRadius(point.like());
    this.id = id2;
  }
  
  protected AbstractCluster(Vector center2, Vector radius2, int id2) {
    this.setNumPoints(0);
    this.setCenter(new RandomAccessSparseVector(center2));
    this.setRadius(new RandomAccessSparseVector(radius2));
    this.id = id2;
  }
  
  @Override
  public void configure(Configuration job) {
    // nothing to do
  }
  
  @Override
  public Collection<Parameter<?>> getParameters() {
    return Collections.emptyList();
  }
  
  @Override
  public void createParameters(String prefix, Configuration jobConf) {
    // nothing to do
  }
  
  /**
   * @param id
   *          the id to set
   */
  protected void setId(int id) {
    this.id = id;
  }
  
  /**
   * @param l
   *          the numPoints to set
   */
  protected void setNumPoints(long l) {
    this.numPoints = l;
  }
  
  /**
   * @param center
   *          the center to set
   */
  protected void setCenter(Vector center) {
    this.center = center;
  }
  
  /**
   * @param radius
   *          the radius to set
   */
  protected void setRadius(Vector radius) {
    this.radius = radius;
  }
  
  // the observation statistics, initialized by the first observation
  private double s0;
  
  private Vector s1;
  
  private Vector s2;
  
  /**
   * @return the s0
   */
  protected double getS0() {
    return s0;
  }
  
  /**
   * @return the s1
   */
  protected Vector getS1() {
    return s1;
  }
  
  /**
   * @return the s2
   */
  protected Vector getS2() {
    return s2;
  }
  
  @Override
  public void observe(Model<VectorWritable> x) {
    AbstractCluster cl = (AbstractCluster) x;
    setS0(getS0() + cl.getS0());
    setS1(getS1().plus(cl.getS1()));
    setS2(getS2().plus(cl.getS2()));
  }
  
  public void observe(ClusterObservations observations) {
    setS0(getS0() + observations.getS0());
    if (getS1() == null) {
      setS1(observations.getS1().clone());
    } else {
      getS1().assign(observations.getS1(), Functions.PLUS);
    }
    if (getS2() == null) {
      setS2(observations.getS2().clone());
    } else {
      getS2().assign(observations.getS2(), Functions.PLUS);
    }
  }
  
  @Override
  public void observe(VectorWritable x) {
    observe(x.get());
  }
  
  @Override
  public void observe(VectorWritable x, double weight) {
    observe(x.get(), weight);
  }
  
  public void observe(Vector x, double weight) {
    if (weight == 1.0) {
      observe(x);
    } else {
      setS0(getS0() + weight);
      Vector weightedX = x.times(weight);
      if (getS1() == null) {
        setS1(weightedX);
      } else {
        getS1().assign(weightedX, Functions.PLUS);
      }
      Vector x2 = x.times(x).times(weight);
      if (getS2() == null) {
        setS2(x2);
      } else {
        getS2().assign(x2, Functions.PLUS);
      }
    }
  }
  
  public void observe(Vector x) {
    setS0(getS0() + 1);
    if (getS1() == null) {
      setS1(x.clone());
    } else {
      getS1().assign(x, Functions.PLUS);
    }
    Vector x2 = x.times(x);
    if (getS2() == null) {
      setS2(x2);
    } else {
      getS2().assign(x2, Functions.PLUS);
    }
  }
  
  @Override
  public long getNumPoints() {
    return numPoints;
  }
  
  public ClusterObservations getObservations() {
    return new ClusterObservations(getS0(), getS1(), getS2());
  }
  
  @Override
  public void computeParameters() {
    if (getS0() == 0) {
      return;
    }
    setNumPoints((int) getS0());
    setCenter(getS1().divide(getS0()));
    // compute the component stds
    if (getS0() > 1) {
      setRadius(getS2().times(getS0()).minus(getS1().times(getS1()))
          .assign(new SquareRootFunction()).divide(getS0()));
    }
    setS0(0);
    setS1(null);
    setS2(null);
  }
  
  @Override
  public void readFields(DataInput in) throws IOException {
    this.id = in.readInt();
    this.setNumPoints(in.readLong());
    VectorWritable temp = new VectorWritable();
    temp.readFields(in);
    this.setCenter(temp.get());
    temp.readFields(in);
    this.setRadius(temp.get());
  }
  
  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(id);
    out.writeLong(getNumPoints());
    VectorWritable.writeVector(out, getCenter());
    VectorWritable.writeVector(out, getRadius());
  }
  
  @Override
  public String asFormatString(String[] bindings) {
    StringBuilder buf = new StringBuilder(50);
    buf.append(getIdentifier()).append("{n=").append(getNumPoints());
    if (getCenter() != null) {
      buf.append(" c=").append(formatVector(getCenter(), bindings));
    }
    if (getRadius() != null) {
      buf.append(" r=").append(formatVector(getRadius(), bindings));
    }
    buf.append('}');
    return buf.toString();
  }
  
  public abstract String getIdentifier();
  
  @Override
  public Vector getCenter() {
    return center;
  }
  
  @Override
  public int getId() {
    return id;
  }
  
  @Override
  public Vector getRadius() {
    return radius;
  }
  
  /**
   * Compute the centroid by averaging the pointTotals
   * 
   * @return the new centroid
   */
  public Vector computeCentroid() {
    return getS0() == 0 ? getCenter() : getS1().divide(getS0());
  }
  
  /**
   * Return a human-readable formatted string representation of the vector, not
   * intended to be complete nor usable as an input/output representation
   */
  public static String formatVector(Vector v, String[] bindings) {
    StringBuilder buf = new StringBuilder();
    if (v instanceof NamedVector) {
      buf.append(((NamedVector) v).getName()).append(" = ");
    }
    int nzero = 0;
    Iterator<Vector.Element> iterateNonZero = v.iterateNonZero();
    while (iterateNonZero.hasNext()) {
      iterateNonZero.next();
      nzero++;
    }
    // if vector is sparse or if we have bindings, use sparse notation
    if (nzero < v.size() || bindings != null) {
      buf.append('[');
      for (int i = 0; i < v.size(); i++) {
        double elem = v.get(i);
        if (elem == 0.0) {
          continue;
        }
        String label;
        if (bindings != null && (label = bindings[i]) != null) {
          buf.append(label).append(':');
        } else {
          buf.append(i).append(':');
        }
        buf.append(String.format(Locale.ENGLISH, "%.3f", elem)).append(", ");
      }
    } else {
      buf.append('[');
      for (int i = 0; i < v.size(); i++) {
        double elem = v.get(i);
        buf.append(String.format(Locale.ENGLISH, "%.3f", elem)).append(", ");
      }
    }
    if (buf.length() > 1) {
      buf.setLength(buf.length() - 2);
    }
    buf.append(']');
    return buf.toString();
  }
  
  @Override
  public long count() {
    return getNumPoints();
  }
  
  @Override
  public boolean isConverged() {
    // Convergence has no meaning yet, perhaps in subclasses
    return false;
  }

  protected void setS0(double s0) {
    this.s0 = s0;
  }

  protected void setS1(Vector s1) {
    this.s1 = s1;
  }

  protected void setS2(Vector s2) {
    this.s2 = s2;
  }
}
