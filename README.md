StoryHoard
==========

Purpose: To create your own choose your own adventure stories

University of Alberta CMPUT301 Fall2013 Team 13 Project.

Refer to the Trello board for project lifecycle stuff: 
https://trello.com/b/eulcxMKA

AUTHORS
=======

Stephanie Gil


LICENSE
=======

Copyright © 2013 Alexander Wong, Ashley Brown, Joshua Tate, Kim Wu, Stephanie Gil

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


Third Party Libraries
=====================


Citing
======




Project Problem Description
===========================

You are to design and implement an attractive, usable, interactive Android application to help satisfy the following user's goals. Your design should be flexible enough to allow future developers to extend it to, for example, add new types of entries (see below).
User's Problem Description

In the 1970s and 1980s Choose Your Own Adventure books were very popular. They were paperback novels which had fragments of stories of scattered across pages. On the bottom of each page was a choice or an ending.

Once you read the fragment which often projected you into a mystical world fraught with peril, you could choose what you could do next. This was done by giving you a list of choices with page numbers. You would then navigate to that page and carry on your adventure.

Sometimes the pages were illustrated.

I want a system where users can create, collaborate, and build their own choose your own adventure story.

I want a system where new choices can be added at any time.

I want a system where stories can be accompanied by images, or videos, or sounds.

I want a system where a user can browse available stories to explore.

I want a system where users can add-on to existing stories.

I would be nice if I could cache some of the stories to read them offline.
Needs in (Partial) User Story Form

User needs are expressed in the form of partial user stories:
as a <role>, I want <goal>
as a <role>, I want <goal> so that <reason>

These descriptions may change to correct omissions and clarify noticed issues.

 As a user, I read story fragments.

As a user, I want to browse available stories.

As an author, I want to publish stories.

As a user, I want to search for available stories.

As an author, I want to add story fragments to an existing story.

As an author, I want to add images to an existing story fragment.

As an author, I want to connect 2 existing story fragments as a choice.

As an author, I want to add a choice to an existing story fragment.

As a user, I want to have cache some stories so that I don't need the internet to work for me to use this program.

As a user, I want to post and publish photos to annotate a fragment, but they will not be the fragment's illustration.

As an author, I want to be able to modify fragments to allow for illustrations to be added.

As a user, I want the fragments to be stored so I can view them offline.

As a user, fragments I view can have multiple illustrations.

As a user, I can upload photos for other stories I did not make, but were downloaded.

As an author, I should be able to retake photos I am taking, if I fail to take the photo I want to correct, so that I don't have erroneous photos.

As a user I should be able to get stories from other users and authors somehow.

New requirements

    None yet.

Notes

As class advances and questions are asked new story cards will be created.
Webservice Notes

Please see the URL: http://www.elasticsearch.org/ and http://www.elasticsearch.org/guide/reference/query-dsl/query-string-query.html

Each group now has its own personal prefix on the elastisearch. Your Android application can communicate to your group's webservice.

Binaries should be small, and you can post them, but be sure you read up on how to send binaries to elastisearch  http://www.elasticsearch.org/guide/reference/mapping/core-types.html http://www.elasticsearch.org/guide/reference/api/admin-indices-put-mapping.html

This webservice is meant to allow posting, listing, deleting and searching recipes. It has a JSON format that can be defined by your group in order to encode more difficult tasks.

http://cmput301.softwareprocess.es:8080/CMPUT301F13T01/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T02/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T03/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T04/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T05/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T07/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T08/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T09/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T10/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T11/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T12/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T13/

http://cmput301.softwareprocess.es:8080/CMPUT301F13T14/


These are not protected in anyway and they should stick to their own repos.

http://cmput301.softwareprocess.es:8080/testing/ is for testing.