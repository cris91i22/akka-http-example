# Akka Http basic example

**What do we have here? It is a basic application, with simple routes but with a complete architecture, from handle the errors, the logs and
store the information into a postgres with Slick 3. I use Akka Actors and Akka Http. To handle properly the errors, I use throughout the
application "Either" monad (from Scalaz), representing the errors in the left side.**

- http routes
- basic actors
- implements scalaz monads (either)
- db storage postgresql (slick 3)
- scala 2.12

## TODO

- Modularize (create paths to actors)
- Use actor selections
- Create an actor like supervisor of others
- Implement routers (by config in akka)
- JWT Authorization
- gatling test
- actors test