//1.- hacer la limpieza de datos necesaria para poder ser procesadocon el siguiente algoritmo
//a.- utilice el algoritmo de machine learning llamado multilayer perceptron que viene el la libreria de spark
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.types._
import org.apache.log4j._
//b.- disene su propia arquiectura con un minimo de tres neuronas de entrada, 
//dos capas en la capa oculta con mas de tres neuronas codad una y 
//finalmente dos o mas neuronas en la capa de salida
Logger.getLogger("org").setLevel(Level.ERROR)

val spark = SparkSession.builder().getOrCreate()

val df = spark.read.option("header","true").option("inferSchema", "true")csv("Iris.csv")

df.show()

df.printSchema()

val Cuchaw =
StructType(
StructField("Tres", DoubleType, true) ::
StructField("Dos", DoubleType, true) ::
StructField("Uno", DoubleType, true) ::
StructField("Cero",DoubleType, true) ::
StructField("Iris-setosa", StringType, true) :: Nil)
//c.- explique detalladamente cada paso del proceso de machine learning dentro del codigo que desarrolle
val df2 = spark.read.option("header", "false").schema(Cuchaw)csv("Iris.csv")
df2.columns

val Etiqueta = new StringIndexer().setInputCol("Iris-setosa").setOutputCol("label")
val Ensamble = new VectorAssembler().setInputCols(Array("Cinco", "Tres", "Uno", "Cero")).setOutputCol("features")

val splits = df2.randomSplit(Array(0.6, 0.4), seed = 1234L)
val train = splits(0)
val test = splits(1)
val neuronas = Array[Int](5, 5, 4, 3)
//d.- explique detalladamente la funcion matematica de entrenamiento que utilizo, consus propias palabras
val trainer = new MultilayerPerceptronClassifier().
 setLayers(neuronas).
 setLabelCol("label").
 setFeaturesCol("features").
 setPredictionCol("prediction").
 setBlockSize(128).
 setSeed(1234L).
 setMaxIter(100)
//e.- explique la funcion de error que utilizo para el resultado final
val pipeline = new Pipeline().setStages(Array(Etiqueta,Ensamble,trainer))

val model = pipeline.fit(train)
val result = model.transform(test)
result.show()
val predictionAndLabels = result.select("prediction", "label")
predictionAndLabels.show()

val evaluator = new MulticlassClassificationEvaluator().setMetricName("accuracy")
println(s"Test set accuracy = ${evaluator.evaluate(predictionAndLabels)}")