import requests
import time
i = 0
with open("location.txt", mode='r') as locs:
    lines = locs.readlines()
    for pline in lines:
        time.sleep(2)
        line = pline.split(',')
        print("Post Location:", i)
        lat = float(line[1])
        lon = float(line[2])
        content = requests.post("http://localhost:8080/postLocation", json={"lat": lat, "lon":lon},headers = {u'content-type': u'application/json', u'charset': 'UTF-8'})
        print("Sent One ... ")
