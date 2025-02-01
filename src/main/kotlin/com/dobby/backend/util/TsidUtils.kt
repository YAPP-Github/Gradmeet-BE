package com.dobby.backend.util

import com.github.f4b6a3.tsid.TsidCreator

fun generateTSID() = TsidCreator.getTsid().toString()
