import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  constructor() { }

  getTopicId(): string{
    return 'myTopicId';
  }
}
