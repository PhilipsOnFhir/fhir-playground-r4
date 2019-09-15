// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  fhirUrl: 'http://localhost:9404/hapi-fhir-jpaserver/fhir/',
  patientSmartApps: [
    { name: 'Event Viewer',
      url: 'http://localhost:4402/launch'
    }
  ],
  imagingStudySmartApps: [
    { name: 'Study Viewer',
      url: 'http://localhost:4241/launch',
      appContext: '{\"imagingStudy\":\"{{%context.studyId}}\"}'
    }
  ]


};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
