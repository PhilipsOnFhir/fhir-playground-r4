package org.github.philipsonfhir.cdshooks.test.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Suggestion {
    String label;
    String uuid;
    List<Action> actions = new ArrayList<>(  );
}
