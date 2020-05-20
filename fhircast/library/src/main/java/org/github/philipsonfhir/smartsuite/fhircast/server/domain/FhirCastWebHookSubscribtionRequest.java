package org.github.philipsonfhir.smartsuite.fhircast.server.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
public class FhirCastWebHookSubscribtionRequest  {
    @JsonProperty("hub.channel.type")  String hub_channel_type ="webhook";
    @JsonProperty("hub.mode")          String hub_mode;
    @JsonProperty("hub.topic")         String hub_topic;
    @JsonProperty("hub.events")        String hub_events;
    @JsonProperty("hub.lease_seconds") String hub_lease_seconds;
    @JsonProperty("hub.callback")      String hub_callback;
    @JsonProperty("hub.secret")        String hub_secret;

    public MultiValueMap<String, String> getFormVars() {
        final MultiValueMap<String, String> formVars = new LinkedMultiValueMap<>();
        formVars.add("hub.channel.type", hub_channel_type );
        formVars.add("hub.mode", hub_mode );
        formVars.add("hub.topic", hub_topic );
        formVars.add("hub.events", hub_events );
        formVars.add("hub.lease_seconds", hub_lease_seconds );
        formVars.add("hub.callback", hub_callback );
        formVars.add("hub.secret", hub_secret );
        return formVars;
    }
}
