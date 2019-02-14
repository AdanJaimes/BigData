//Examen 1 
//Datos Masivos
//Unidad 1

//1. Verifique solo número par:
//a. Escriba una función que tome un entero y devuelva un booleano que indique si es par o no.

scala> def isEven(num:Int): Boolean = {
return num%2 == 0
}

def Par(num:Int): Boolean = num%2 == 0
println(isEven(6))
println(isEven(3))

Par(2)
res1: Boolean = true
Par(3)
res2: Boolean = false



//2. Buscar números pares en lista.
//a. Escriba una función que devuelva True si hay un número par dentro de una Lista: de lo contrario devuelve False.

scala> def listEvens(list:List[Int]): String ={
for(n <- list){
if(n%2==0){
println(s"$n is True")
}else{
println(s"$n is False")
}
}
return "Done"
}

val l = List(1,2,3,4,5,6,7,8)
val l2 = List(4,3,22,55,7,8)
listEvens(l)
listEvens(l2)



//3. Afortunado número 7:
//a. Tome una lista de enteros y calcule su suma. Sin embargo, los sietes tienen suerte y deben contarse dos veces, lo que significa que su valor es 14 para la suma.

scala> def afortunado(list:List[Int]): Int={
var res=0
for(n <- list){
if(n==7){
res = res + 14
}else{
res = res + n
}
}
return res
}

val af= List(1,7,7)
println(afortunado(af))

//4. Puede equilibrar?
//a. Dada una lista no vacía de enteros, devuelve verdadero si hay un lugar para dividir la lista de modo que la suma de los números en un lado sea igual a la suma de los números en el otro lado.

scala> def balance(list:List[Int]): Boolean={
var primera = 0
var segunda = 0

segunda = list.sum

for(i <- Range(0,list.length)){
primera = primera + list(i)
segunda = segunda - list(i)

if(primera == segunda){
return true
}
}
return false
}

val bl = List(3,2,1)
val bl2 = List(2,3,3,2)
val bl3 = List(10,30,90)

balance(bl)
balance(bl2)
balance(bl3)

//5. Verificar palíndromo:
//a. Dada una cadena, devuelva un boleano que indica si es o no un palindromo.
scala> def palindromo(palabra:String):Boolean ={
return (palabra == palabra.reverse)
}

val palabra = "OSO"
val palabra2 = "ANNA"
val palabra3 = "JUAN"

println(palindromo(palabra))
println(palindromo(palabra2))
println(palindromo(palabra3))

