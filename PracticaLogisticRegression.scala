//////////////////////////////////////////////
// Proyecto de regresion logistica //////////////
////////////////////////////////////////////

//  In this project we will be working with a fake advertising data set, indicating whether or not a particular internet user clicked on an Advertisement. We will try to create a model that will predict whether or not they will click on an ad based off the features of that user.
//  This data set contains the following features:
//    'Daily Time Spent on Site': consumer time on site in minutes
//    'Age': cutomer age in years
//    'Area Income': Avg. Income of geographical area of consumer
//    'Daily Internet Usage': Avg. minutes a day consumer is on the internet
//    'Ad Topic Line': Headline of the advertisement
//    'City': City of consumer
//    'Male': Whether or not consumer was male
//    'Country': Country of consumer
//    'Timestamp': Time at which consumer clicked on Ad or closed window
//    'Clicked on Ad': 0 or 1 indicated clicking on Ad

//////////////////////////////////////////////////////////
// Complete las siguientes tareas que estan comentas ////
/////////////////////////////////////////////////////////



////////////////////////
/// Tome los datos //////
//////////////////////

// Importe una  SparkSession con la libreria Logistic Regression
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession
// Optional: Utilizar el codigo de  Error reporting
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

// Cree un sesion Spark 
val spark = SparkSession.builder().getOrCreate()

// Utilice Spark para leer el archivo csv Advertising.
val data  = spark.read.option("header","true").option("inferSchema", "true").format("csv").load("advertising.csv")
// Imprima el Schema del DataFrame
data.printSchema()
///////////////////////
/// Despliegue los datos /////
/////////////////////

// Imprima un renglon de ejemplo 


////////////////////////////////////////////////////
//// Preparar el DataFrame para Machine Learning ////
//////////////////////////////////////////////////

//   Hacer lo siguiente:
//    - Renombre la columna "Clicked on Ad" a "label"
//    - Tome la siguientes columnas como features "Daily Time Spent on Site","Age","Area Income","Daily Internet Usage","Timestamp","Male"
//    - Cree una nueva clolumna llamada "Hour" del Timestamp conteniendo la  "Hour of the click"
data.select("Clicked on Ad").show()
val timedata = data.withColumn("Hour",hour(data("Timestamp")))

val logregdata = (timedata.select(data("Clicked on Ad").as("label"),
                 $"Daily Time Spent on Site",$"Age",$"Area Income",
                 $"Daily Internet Usage",$"Hour",$"Male")
                 )
// Importe VectorAssembler y Vectors
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
// Cree un nuevo objecto VectorAssembler llamado assembler para los feature
val assembler = ( new VectorAssembler().setInputCols(
                Array("Daily Time Spent on Site","Age","Area Income",
                "Daily Internet Usage","Hour","Male")).setOutputCol("features")
)

// Utilice randomSplit para crear datos de train y test divididos en 70/30
val Array(training,test) = logregdata.randomSplit(Array(0.7,0.3),seed=12345)

///////////////////////////////
// Configure un Pipeline ///////
/////////////////////////////

// Importe  Pipeline
import org.apache.spark.ml.Pipeline
// Cree un nuevo objeto de  LogisticRegression llamado lr
val lr = new LogisticRegression()
// Cree un nuevo  pipeline con los elementos: assembler, lr
val pipeline = new Pipeline().setStages(Array(assembler,lr))
// Ajuste (fit) el pipeline para el conjunto de training.
val model = pipeline.fit(training)
// Tome los Resultados en el conjuto Test con transform
val results = model.transform(test)

////////////////////////////////////
//// Evaluacion del modelo /////////////
//////////////////////////////////
// Para Metrics y Evaluation importe MulticlassMetrics
import org.apache.spark.mllib.evaluation.MulticlassMetrics
// Convierta los resutalos de prueba (test) en RDD utilizando .as y .rdd
val predictionAndLabels = results.select($"prediction",$"label").as[(Double,Double)].rdd
// Inicialice un objeto MulticlassMetrics 
val metrics = new MulticlassMetrics(predictionAndLabels)
// Imprima la  Confusion matrix
println("confusion Matrix ")
println(metrics.confusionMatrix)