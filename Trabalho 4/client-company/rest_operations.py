import requests as r
import json

url = 'http://localhost:3000'

'''
    Encapsulamento da operação GET para busca de entidades
    entity: string -> entidade com a qual se deseja interagir (curriculum ou jobOffer)
    filter_parameters: dicionário -> parâmetros de filtro para aplicar na busca de entidades,
                                     eventualmente transformado em uma query string
'''
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

'''
    Encapsulamento da operação POST para registro de entidades
    entity: string -> entidade com a qual se deseja interagir (curriculum ou jobOffer)
    data: dicionário -> contém os dados a respeito da entidade que será registrada,
                        passado como body da request
'''
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

'''
    Encapsulamento da operação POST para atualização de entidades
    entity: string -> entidade com a qual se deseja interagir (curriculum ou jobOffer)
    data: dicionário -> contém os dados da entidade a serem atualizados, incluindo seu
                        ID único. É passado no body da request
'''
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
