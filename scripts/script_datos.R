##MUESTREO SECUENCIAL DE PRECISIÓN FIJA CON PARAMETROS DE TAYLOR##
#CULTIVO: Mango
#Plaga: Aulacaspis tubercularis (Escama)

DATOS<- c(0.0, 0.0, 0.0, 0.0, 2.0, 1.0, 0.0, 3.0, 0.0, 0.0, 0.0, 2.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 1.0, 0.0, 0.0, 2.0, 1.0, 3.0, 1.0, 0.0);

> #Densidad estimada
> mean(DATOS)
[1] 0.5945946
> #relacion varianza media
> var(DATOS)/mean(DATOS)
[1] 1.35101
> #Error estandar
> sd(DATOS)/sqrt(length(DATOS))
[1] 0.1473462
> round(sd(DATOS)/sqrt(length(DATOS)),2)
[1] 0.15
> #Precision alcanzada
>  (1 - sd(DATOS)/((sqrt(length(DATOS)))*mean(DATOS)))*100
[1] 75.21905
> 


##MUESTREO SECUENCIAL DE PRECISIÓN FIJA SIN PARAMETROS DE TAYLOR##
#CULTIVO: Mango 
#PLAGA: Frankliniella occidentalis (Trips)

DATOS<- c(2.0, 1.0, 0.0, 0.0, 2.0, 1.0, 3.0, 0.0, 0.0, 0.0, 2.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 2.0, 1.0, 0.0, 0.0, 0.0,0.0, 2.0, 1.0);

> #media
> mean(DATOS)
[1] 0.8
> #relacion varianza media
> var(DATOS)/mean(DATOS)
[1] 1.041667
> #Error estandar
>  sd(DATOS)/sqrt(length(DATOS))
[1] 0.1825742
> #precision alcanzada
> (1 - sd(DATOS)/((sqrt(length(DATOS)))*mean(DATOS)))*100
[1] 77.17823
> 

#MUESTREO DE PROPORCION#
#CULTIVO: Mango
#PLAGA: Ormenis pulverulenta (Papalota del mango)

DATOS<- c(1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0,0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0);

> #Proporcion estimada
> mean(DATOS)
[1] 0.3414634
> #Relacion varianza media
> var(DATOS)/mean(DATOS)
[1] 0.675
> #Error estandar
> sd(DATOS)/sqrt(length(DATOS))
[1] 0.07497769
> #Precision alcanzada
> (1 - 1.95*(sqrt(mean(DATOS)*(1-mean(DATOS))))/sqrt(length(DATOS)))*100
[1] 85.55875
> 

