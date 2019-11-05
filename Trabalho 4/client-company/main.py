import requests
import json

url = 'http://localhost:3000'

def main():
    r = requests.get(url + '/curriculum/get')
    print(r.status_code)
    print(r.text)

    res = json.loads(r.text)
    print(res)

    print(res[0]["area"])

if __name__ == "__main__":
    main()