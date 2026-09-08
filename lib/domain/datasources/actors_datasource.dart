import 'package:cinemapedia/domain/entities/actor.dart';



// contrato para actores, solo un metodo
abstract class ActorsDatasource {

  Future<List<Actor>> getActorsByMovie( String movieId );

}
