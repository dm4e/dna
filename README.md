# Proyecto ADN

Este peroyecto cumple con el siguiente [Ejercicio](http://github.com/dm4e/dna/blob/master/doc/Ejercicio.pdf)


## Servicios
La aplicación expone los siguientes servicios:

### Análisis de ADN

```
curl -X POST -H 'Accept: application/json' -H 'Content-Type: application/json' -i 'http://18.188.208.223:8080/mutant' --data '{
 "dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"] 
}'
```

Respuestas

 - `200` análisis determinó ADN mutante

 - `403` análisis determinó ADN humano

 - `400` análisis incompleto por error en los datos del request


### Estadísticas de análisis

```
curl -X GET -H 'Accept: application/json' -H 'Content-Type: application/json' -i 'http://18.188.208.223:8080/stats' 
```

## Tecnología

  - Maven 
  - Java 8
  - spring framework
  - Jetty servlet
  - [DynamoDB](https://docs.aws.amazon.com/es_es/amazondynamodb/latest/developerguide/DynamoDBLocal.html)

## StartUp
```
mvn clean compile exec:java -Damazon.aws.accesskey={secret1} -Damazon.aws.secretkey={secret2}
```

## Cobertura
<a href="https://github.com/dm4e/dna/blob/master/coverage/jacoco-ut/jacoco.csv" target="_blank"><img src="https://github.com/dm4e/dna/blob/master/doc/cobertura.png" 
alt="Cobertura" width="840" height="180" border="10" /></a>
