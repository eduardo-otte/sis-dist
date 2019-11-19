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

    get_response = {
        "status_code": response.status_code
    }

    if response.status_code == 200:
        get_response["body"] = json.loads(response.text)

    return get_response

def register(entity, data):
    request_url = url + '/' + entity + '/register'

    try:
        response = r.post(request_url, json = data)
    except:
        print("Error while performing POST")

    if response.status_code == 200:
        parsed_body = json.loads(response.text)
        return parsed_body

    else:
        return response.status_code

def update(entity, data):
    request_url = url + '/' + entity + '/update'

    try:
        response = r.post(request_url, data)
    except:
        print("Error while performing POST")

    if response.status_code == 200:
        parsed_body = json.loads(response.text)
        return parsed_body

    else:
        return response.status_code
