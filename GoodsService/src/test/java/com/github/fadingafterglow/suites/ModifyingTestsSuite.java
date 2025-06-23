package com.github.fadingafterglow.suites;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.github.fadingafterglow")
@ExcludeTags("read")
public class ModifyingTestsSuite {
}
