import 'package:cinemapedia/domain/entities/actor.dart';


// misma idea que movies_repository pero para actores
abstract class ActorsRepository {

  Future<List<Actor>> getActorsByMovie( String movieId );

}
