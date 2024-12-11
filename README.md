# GymSpringCore
First Project in EPAM Java Internship.

Project where we try to create an application with console interface. It will be a gym CRM, where users can register as trainee or trainer.
Several Service classes that will work with Repository/Dao objects. Our tool is Spring Core, so dependency injection should not be a problem.

Database is basically a file which gets read with every start. After reading it, I will store all the users, trainings and other objects in heap memory of computer. The datastucture used to store is Map<>, I will use HashMap<> as it's amortised complexity is smaller than other implementations. Entities used in app have pretty simple relationship. All trainees and trainers are generalisation of User. Training depends on Trainee and Trainer. Training type depends on Training and Trainer.

The main challenge I will probably face is how to make program keep running, when I do not have a Spring Boot. But I already have an Idea.

docker run --detach --name mycontainer -p 61616:61616 -p 8161:8161 --rm apache/activemq-artemis:latest-alpine