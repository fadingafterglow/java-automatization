package com.github.fadingafterglow.suites;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.github.fadingafterglow")
@IncludeTags("read")
public class ReadTestsSuite {
}
