import rest_operations as ro

url = 'http://localhost:3000'

def main():
    response = ro.get('curriculum', { })

    print(response)

if __name__ == "__main__":
    main()
