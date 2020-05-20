package org.github.philipsonfhir.smartsuite.local.cli;

import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastClient;
import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastClientTopic;
import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastWebhookClient;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.WorkflowEventFactory;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class MyWebHookClientCli {

    private final FhirCastClient fhirCastClient;
    private FhirCastClientTopic fhirCastClientTopic;
    private FhirCastWebhookClient fhirCastWebhookClient;

    public MyWebHookClientCli(){
        fhirCastClient = new FhirCastClient( "http://localhost:9410");
    }


    @ShellMethod("list topics")
    public String listTopics() throws FhirCastException {
        List<FhirCastWebhookClient> a = fhirCastClient.getTopics();// invoke service
        List<String> topics = a.stream()
                .map( fhirCastWebhookClient -> fhirCastWebhookClient.getTopicId() ).collect(Collectors.toList());
        String result = "";
        for( String topic: topics ){
            result += "topic:\t"+topic+"\n";
        }
        return result;
    }

    @ShellMethod("Create topic.")
    public String create() throws FhirCastException {
        fhirCastClientTopic = fhirCastClient.createTopic();
        fhirCastWebhookClient = fhirCastClientTopic.createWebHookClient();
        return "connected to topic "+fhirCastClientTopic.getTopicId();
    }

    @ShellMethod("Connect to topic.")
    public String connect(
            @ShellOption() String topic
    ) throws FhirCastException {
        fhirCastClientTopic = fhirCastClient.connectToTopic(topic);
        fhirCastWebhookClient = fhirCastClientTopic.createWebHookClient();
        return "connected";
    }

    @ShellMethod("Connect to first topic.")
    public String connectFirst(
            @ShellOption() String topic
    ) throws FhirCastException {
        List<FhirCastWebhookClient> a = fhirCastClient.getTopics();// invoke service
        List<String> topics = a.stream()
                .map( fhirCastWebhookClient -> fhirCastWebhookClient.getTopicId() ).collect(Collectors.toList());
        if ( topics.size()==0 ){
            return "No topics defined yet";
        }
        fhirCastClientTopic = fhirCastClient.connectToTopic(topics.get(0));
        fhirCastWebhookClient = fhirCastClientTopic.createWebHookClient();
        return "connected to "+fhirCastClientTopic.getTopicId();
    }

    @ShellMethod("logout")
    public String logout() throws FhirCastException {
        if ( fhirCastClientTopic==null ){
            return "not connected to a topic";
        }
        fhirCastClientTopic.logOut();
        return "logout";
    }

    @ShellMethod("subscribe to event(s) <event(s)>")
    public String subscribe(
            @ShellOption() String events
    ) throws FhirCastException {
        if ( fhirCastWebhookClient==null ){
            return "not connected to a topic";
        }
        fhirCastWebhookClient.subscribe( events, 3600 );
        System.out.println("wait for verification");
        fhirCastWebhookClient.waitForVerfication();
        System.out.println("verified");
        return fhirCastWebhookClient.subscribedEvents();
    }

    @ShellMethod("unsubscribe to event(s) <event(s)>")
    public String unsubscribe(
            @ShellOption() String events
    ) throws FhirCastException {
        if ( fhirCastWebhookClient==null ){
            return "not connected to a topic";
        }
        fhirCastWebhookClient.unsubscribe( events );
        return "unsubscribed";
    }

    @ShellMethod("send event")
    public String send(
            @ShellOption() String event,
            @ShellOption() String id
    ) throws FhirCastException {
        if ( fhirCastWebhookClient==null ){
            return "not connected to a topic";
        }

        ContextEvent contextEvent = null;
        switch ( event ){
            case "patient-open":
                contextEvent = WorkflowEventFactory.createOpenEvent((Patient) new Patient().setId(id));
                break;
            case "patient-close":
                contextEvent = WorkflowEventFactory.createCloseEvent((Patient) new Patient().setId(id));
                break;
            case "imagingstudy-open":
                contextEvent = WorkflowEventFactory.createOpenEvent((Patient) new Patient().setId(id));
                break;
            case "imagingstudy-close":
                contextEvent = WorkflowEventFactory.createCloseEvent((Patient) new Patient().setId(id));
                break;
            default:
                return "unknown event";
        }
        System.out.println( "send event "+contextEvent );
        contextEvent.getEvent().setHub_topic( this.fhirCastClientTopic.getTopicId() );
        fhirCastClientTopic.sendEvent( contextEvent );
        return "event sending done";
    }

    @ShellMethod("Show status.")
    public String status() {
        String result = "";
        result += (this.fhirCastClientTopic!=null?"topic: "+this.fhirCastClientTopic.getTopicId():"disconnected");
        result += "\n";
        result += (this.fhirCastWebhookClient!=null?"webhook connected":"webhook disconnected");
        result += "\n";
        result += (this.fhirCastWebhookClient!=null?fhirCastWebhookClient.subscribedEvents()+"":"");
        result += "\n";
        result += (this.fhirCastWebhookClient!=null?"last event\t: "+this.fhirCastWebhookClient.getLastEvent():"");
        result += "\n";
        return result;
    }

    @ShellMethod("Echo input.")
    public String echo(
            @ShellOption() String text
    ) {
        // invoke service
        return "echo: "+text;
    }
}
