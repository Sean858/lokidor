from japronto import Application

# Views handle logic, take request as a parameter and
# returns Response object back to the client

import datetime
from numpy import sin, cos, sqrt, arctan2, radians
import pandas as pd
import numpy as np

R = 6373.0 * 1000
columns_name = ["time", "latitude", "longitude"]
df = pd.read_csv('./location.txt', delimiter=",", header=None, names=columns_name)
df = df.set_index(pd.DatetimeIndex(df['time']))
neighbour_number = 5
nearby_distance = 100


def calculate_distance(latitude, longitude, new_point):
    lat1 = radians(latitude)
    lon1 = radians(longitude)
    lat2 = radians(new_point[0])
    lon2 = radians(new_point[1])

    dlon = lon2 - lon1
    dlat = lat2 - lat1

    a = sin(dlat / 2) ** 2 + cos(lat1) * cos(lat2) * sin(dlon / 2) ** 2
    c = 2 * arctan2(sqrt(a), sqrt(1 - a))

    distance = R * c
    return distance


def get_time_before_and_after_half_hour(current_time):
    current_time = datetime.datetime.strptime(current_time, "%Y-%m-%d-%H:%M:%S")
    time_before = current_time - datetime.timedelta(minutes=30)
    time_after = current_time + datetime.timedelta(minutes=30)
    # Convert datetime to string
    time_before = time_before.strftime("%H:%M")
    time_after = time_after.strftime("%H:%M")
    return [time_before, time_after]


def get_distance(current_time, new_point):
    time_before, time_after = get_time_before_and_after_half_hour(current_time)
    valid_time_range = df.between_time(time_before, time_after)
    history_latitude = valid_time_range["latitude"]
    history_longitude = valid_time_range["longitude"]
    distance = calculate_distance(history_latitude, history_longitude, new_point)
    point_in_range = np.sum(distance < nearby_distance)
    fit = True if point_in_range > neighbour_number else False
    return point_in_range, fit


def hello(request):
    info = request.json
    print(info)
    current_time = info["time"]
    latitude = float(info["lat"])
    longitude = float(info["lon"])
    point_in_range, fit = get_distance(current_time, (latitude, longitude))
    return request.Response(
        code=200,
        json={
            "status": fit,
            "message": str(point_in_range) + " Points in " + str(nearby_distance) + "m"
        },
    )


# The Application instance is a fundamental concept.
# It is a parent to all the resources and all the settings
# can be tweaked here.
app = Application()

# The Router instance lets you register your handlers and execute
# them depending on the url path and methods
app.router.add_route('/check_location', hello, methods=["POST"])

# Finally start our server and handle requests until termination is
# requested. Enabling debug lets you see request logs and stack traces.
app.run(debug=True)
