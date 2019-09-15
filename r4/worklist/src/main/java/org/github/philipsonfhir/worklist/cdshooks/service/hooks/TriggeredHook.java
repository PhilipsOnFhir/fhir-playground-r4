package org.github.philipsonfhir.worklist.cdshooks.service.hooks;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TriggeredHook {
    String hook;
    String hookInstance = "HI"+System.nanoTime();
    Map<String,String> context = new HashMap<>();


}
