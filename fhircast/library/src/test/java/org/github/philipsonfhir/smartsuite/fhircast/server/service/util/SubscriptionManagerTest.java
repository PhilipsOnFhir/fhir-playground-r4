package org.github.philipsonfhir.smartsuite.fhircast.server.service.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionManagerTest {
    @Test
    public void getEvents1() {
        String event = "patient-open,patient-close";
        SubscriptionManager subscriptionManager = new SubscriptionManager();
        subscriptionManager.addSubscriptions( event );
        assertTrue( subscriptionManager.hasSubscription( "patient-open"));
        assertFalse( subscriptionManager.hasSubscription( "other-open"));
    }

    @Test
    public void getEvents2() {
        String event = "patient-*";
        SubscriptionManager subscriptionManager = new SubscriptionManager();
        subscriptionManager.addSubscriptions( event );
        assertTrue( subscriptionManager.hasSubscription(  "patient-open"));
        assertFalse( subscriptionManager.hasSubscription( "ima-open"));
    }

    @Test
    public void getEvents3() {
        String event = "*-close";
        SubscriptionManager subscriptionManager = new SubscriptionManager();
        subscriptionManager.addSubscriptions( event );
        assertFalse( subscriptionManager.hasSubscription(  "patient-open"));
        assertTrue( subscriptionManager.hasSubscription(  "ima-close"));
    }

    @Test
    public void getEvents4() {
        String event = "*-*";
        SubscriptionManager subscriptionManager = new SubscriptionManager();
        subscriptionManager.addSubscriptions( event );
        assertTrue( subscriptionManager.hasSubscription(  "patient-open"));
        assertTrue( subscriptionManager.hasSubscription(  "ima-open"));
    }

    @Test
    public void getEventsLogout() {
        String event = "*";
        SubscriptionManager subscriptionManager = new SubscriptionManager();
        subscriptionManager.addSubscriptions( event );
        assertTrue( subscriptionManager.hasSubscription(  "patient-open"));
        assertTrue( subscriptionManager.hasSubscription(  "userlogout"));
    }

    @Test
    public void updateEvents1() {
        String event = "patient-open,patient-close";
        SubscriptionManager subscriptionManager = new SubscriptionManager();
        subscriptionManager.addSubscriptions( event );
        assertTrue( subscriptionManager.hasSubscription(  "patient-open"));
        assertTrue( subscriptionManager.hasSubscription(  "patient-close"));
        assertFalse( subscriptionManager.hasSubscription(  "imagestudy-close"));
        assertFalse( subscriptionManager.hasSubscription(  "userlogout"));

        subscriptionManager.addSubscriptions( "userlogout" );
        assertTrue( subscriptionManager.hasSubscription(  "patient-open"));
        assertTrue( subscriptionManager.hasSubscription(  "patient-close"));
        assertFalse( subscriptionManager.hasSubscription(  "imagestudy-close"));
        assertTrue( subscriptionManager.hasSubscription(  "userlogout"));

        subscriptionManager.removeSubscriptions( "patient-open" );
        assertFalse( subscriptionManager.hasSubscription(  "patient-open"));
        assertTrue( subscriptionManager.hasSubscription(  "patient-close"));
        assertFalse( subscriptionManager.hasSubscription(  "imagestudy-close"));
        assertTrue( subscriptionManager.hasSubscription(  "userlogout"));

    }

    @Test
    public void updateEvents2() {
        String event = "*-*";
        SubscriptionManager subscriptionManager = new SubscriptionManager();
        subscriptionManager.addSubscriptions( event );
        assertTrue( subscriptionManager.hasSubscription(  "patient-open"));
        assertTrue( subscriptionManager.hasSubscription(  "patient-close"));
        assertTrue( subscriptionManager.hasSubscription(  "imagestudy-close"));
        assertFalse( subscriptionManager.hasSubscription(  "userlogout"));

        subscriptionManager.removeSubscriptions( "userlogout" );
        assertTrue( subscriptionManager.hasSubscription(  "patient-open"));
        assertTrue( subscriptionManager.hasSubscription(  "patient-close"));
        assertTrue( subscriptionManager.hasSubscription(  "imagestudy-close"));
        assertFalse( subscriptionManager.hasSubscription(  "userlogout"));

        subscriptionManager.addSubscriptions( "*" );
        assertTrue( subscriptionManager.hasSubscription(  "patient-open"));
        assertTrue( subscriptionManager.hasSubscription(  "patient-close"));
        assertTrue( subscriptionManager.hasSubscription(  "imagestudy-close"));
        assertTrue( subscriptionManager.hasSubscription(  "userlogout"));

    }

    @Test
    public void updatePrivateEvent() {
        {
            String event = "*-*";
            SubscriptionManager subscriptionManager = new SubscriptionManager();
            subscriptionManager.addSubscriptions(event);
            assertTrue(subscriptionManager.hasSubscription("com.philips.igt/context-change"));
        }
        {
            String event = "*";
            SubscriptionManager subscriptionManager = new SubscriptionManager();
            subscriptionManager.addSubscriptions(event);
            assertTrue(subscriptionManager.hasSubscription("com.philips.igt/context-change"));
        }
    }
}
