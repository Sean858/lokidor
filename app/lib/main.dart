import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:location/location.dart';
import 'package:http/http.dart' as http;
import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:intl/intl.dart';
import 'package:battery/battery.dart';



void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
            body: FireMap()
        )
    );
  }
}

class FireMap extends StatefulWidget {
  @override
  State createState() => FireMapState();
}


class FireMapState extends State<FireMap> {

  @override
  void initState() {
    super.initState();
    postPath();
    startTimer();
  }

  GoogleMapController mapController;
  Location location = new Location();
  Battery _battery = new Battery();

  void startTimer() async {
    new Timer.periodic(Duration(seconds: 10), (timer) {
      _postLocation();
//      print("Post Success");
      getBatteryStatus();
    });
  }

  void getBatteryStatus() async {
    final int batteryLevel = await _battery.batteryLevel;
    print('$batteryLevel%');
  }

//  Future<int> batteryStatus() async {
//    var battery = await Battery().batteryLevel;
//// Access current battery level
//    return battery;
//  }

  build(context) {
    return Stack(
        children: [
          GoogleMap(
              initialCameraPosition: CameraPosition(target: LatLng(24.150, -110.32), zoom: 10),
              onMapCreated: _onMapCreated,
              myLocationEnabled: true, // Add little blue dot for device location, requires permission from user
              mapType: MapType.hybrid,
              trackCameraPosition: true
          ),
          Positioned(
              bottom: 100,
              right: 10,
              child:
              FlatButton(
                  child: Icon(Icons.pin_drop),
                  color: Colors.green,
                  onPressed: () => _animateToUser()
              )
          ),

          Positioned(
              bottom: 150,
              right: 10,
              child:
              FlatButton(
                  child: Icon(Icons.add_alert),
                  color: Colors.red,
                  onPressed: () => _animateToUser()
              )
          ),
        ]
    );
  }

  _addMarker() {
    var marker = MarkerOptions(
        position: mapController.cameraPosition.target,
        icon: BitmapDescriptor.defaultMarker,
        infoWindowText: InfoWindowText('Magic Marker', '🍄🍄🍄')
    );
    mapController.addMarker(marker);
  }

  void _onMapCreated(GoogleMapController controller) {
    setState(() {
      mapController = controller;
    });
  }
//10.0.0.144

  _animateToUser() async {
    var pos = await location.getLocation();
    print('position:' + 'latitude: ' + pos['latitude'].toString() + 'longitude: ' + pos['longitude'].toString());
    mapController.animateCamera(CameraUpdate.newCameraPosition(
        CameraPosition(
          target: LatLng(pos['latitude'], pos['longitude']),
          zoom: 17.0,
        )
    )
    );
  }

  void _postLocation() async{
    var pos = await location.getLocation();
//    var url = "http://127.0.0.1:8080/postLocation";
//    var url = "http://10.0.0.80:8080/postLocation";
//    var url = "http://10.215.60.234:8080/postLocation";
    var url = "http://10.215.24.162:8080/postLocation";
    Map data = {
      "lat": pos['latitude'].toString(),
      "lon": pos['longitude'].toString()
    };
    String formattedDate = DateFormat('yyyy-MM-dd-kk:mm:ss').format(new DateTime.now());
    var storeLocation = formattedDate + ',' + pos['latitude'].toString() + ',' + pos['longitude'].toString() + '\n';
    writeLocation(storeLocation);
    http.post(url,
        headers: {"Content-Type": "application/json"},
        body: json.encode(data))
        .then((response) {
      print("Post server success");
//      writeLocation(storeLocation);
//      print("Response status: ${response.statusCode}");
//      print("Response body: ${response.body}");
    });

    var urlData = "http://10.215.40.108:8080/check_location";

    Map jsonData = {
      "time": formattedDate,
      "lat": pos['latitude'].toString(),
      "lon": pos['longitude'].toString()
    };

    http.post(urlData,
        headers: {"Content-Type": "application/json"},
        body: json.encode(jsonData))
        .then((response) {
      print("Post Datacenter success");
//      writeLocation(storeLocation);
//      print("Response status: ${response.statusCode}");
//      print("Response body: ${response.body}");
    });
  }

  void postPath() async{
//    var url = "http://10.0.0.80:8080/postPath";
//    var url = "http://10.215.60.234:8080/postPath";
    var url = "http://10.215.24.162:8080/postPath";
    String data = await readLocation();
    http.post(url,
        body: data)
        .then((response) {
      print("Postpath success!");
    });

//    _resetFile();
  }

  Future<String> get _localPath async {
    final Directory tempDir = await getTemporaryDirectory();
    String tempPath = tempDir.path;
    return tempPath;
  }

  Future<File> get _localFile async {
    final path = await _localPath;
    return File('$path/location.txt');
  }

  Future<File> _resetFile() async {
    final file = await _localFile;
    return file.writeAsString('Start:\n', mode: FileMode.write, flush: true);
  }

  Future<File> writeLocation(String location) async {
    final file = await _localFile;
    // Write the file
//    return file.writeAsString('$location');
    return file.writeAsString(location, mode: FileMode.append, flush: true);
  }

  Future<String> readLocation() async {
    try {
      final file = await _localFile;
      // Read the file
//      String contents = await file.readAsString();
      String contents = await file.readAsString();

      return contents;
    } catch (e) {
      // If encountering an error, return 0
      return 'Error';
    }
  }


}


