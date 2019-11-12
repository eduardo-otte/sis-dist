import rest_operations as ro

url = 'http://localhost:3000'

def main():
    """
    companyName": "Test McTest III",
	"area": "Fish hunting",
	"contact": "Smoke my dudes",
	"salary": 5000,
	"workload": 40"""

    aux = {'companyName': "MyCompany", 'area': "MyArea", 'contact': "MyContact", 'salary': 1000,'workload':30} 
    aux2 = {'companyName': "MyCompany", 'area': "MyArea", 'contact': "MyContact", 'salary': 10000,'workload':20} 

    response = ro.register('jobOffer', aux)
    response = ro.register('jobOffer', aux2)


    aux1 = {'id': 1, 'salary': 2000,'workload':20} 

    response = ro.update('jobOffer', aux1)
    


if __name__ == "__main__":
    main()
