package com.velmurugan.updatelivelocation



data class MyLatLang(var lat:Double, var lang:Double,var routeNo:String,var driverName:String,var driverMobileNo:String,var driverBusNo:String){

    constructor() : this(0.000000,0.0000000,"","","","")

}