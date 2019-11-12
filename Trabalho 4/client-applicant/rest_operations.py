import requests as r
import json

url = 'http://localhost:3000'

def get(entity, filter_parameters):
    query_string = '?'
    for key in filter_parameters.keys():
        query_string += str(key).replace(' ', '%20')
        query_string += '='
        query_string += str(filter_parameters[key]).replace(' ', '+')
        query_string += '&'

    request_url = url + '/' + entity + '/get' + query_string

    try:
        response = r.get(request_url)
    except:
        print("Error while performing GET")

    if response.status_code == 200:
        print("GET performed successfully")
        parsed_response = json.loads(response.text)
        print(parsed_response)

        return parsed_response

    elif response.status_code == 400:
        print("Bad request")

    elif response.status_code == 404:
        print("Not found")

    else:
        print("Request return status: " + response.status_code)


def register(entity, data):
    request_url = url + '/' + entity + '/register'

    try:
        response = r.post(request_url, json = data)
    except:
        print("Error while performing POST")

    if response.status_code == 200:
        print("POST performed successfully")
        print(response.text)
        return response.text

    elif response.status_code == 400:
        print("Bad request: " + response.text)

    else:
        print("Request return status: " + response.status_code)
        print(response.text)


def update(entity, data):
    request_url = url + '/' + entity + '/update'

    try:
        response = r.post(request_url, data)
    except:
        print("Error while performing POST")

    if response.status_code == 200:
        print("POST performed successfully")
        print(response.text)

        return response.text

    elif response.status_code == 400:
        print("Bad request: " + response.text)

    elif response.status_code == 404:
        print("Not found")

    else:
        print("Request return status: " + response.status_code)
        print(response.text)
