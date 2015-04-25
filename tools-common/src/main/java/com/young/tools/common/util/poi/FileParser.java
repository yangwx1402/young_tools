package com.young.tools.common.util.poi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileParser {

	public Doc parse(File file,String encode) throws FileNotFoundException, IOException;
}
