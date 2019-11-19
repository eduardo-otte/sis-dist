import rest_operations as ro
from types import *

url = 'http://localhost:3000'



def printJobs(jobs):
    i = 1
    for job in jobs:
        print()
        print("Vaga: " + str(i))
        print(" - Nome da empresa: " + job['companyName'])
        print(" - Área: " + job['area'])
        print(" - Contato: " + job['contact'])
        print(" - Salário : " + str(job['salary']))
        print(" - Carga horária: " + str(job['workload']))
        i += 1


def main():
    selectedOption = 0
    hasCurriculum = False
    curriculum = { }

    while(selectedOption != 4):
        print("---------------------------")
        print("Selecione a opção desejada:")
        print("1) Enviar currículo")
        print("2) Alterar dados do currículo")
        print("3) Checar vagas cadastradas")
        print("4) Encerrar")

        selectedOption = int(input())

        #1) Enviar currículo
        if (selectedOption == 1) and hasCurriculum:
            print("Você já possui um curriculo cadastrado. Se você quiser atualizar seus dados, selecione a opção 2")

        elif (selectedOption == 1) and (not hasCurriculum):

            name = input("Nome: ")
            area = input("Área de Interesse: ")
            contact = input("Contato: ")
            intendedSalary = float(input("Salário pretendido: "))
            workload = int(input("Carga horária pretendida: "))

            curriculum['name'] = name
            curriculum['area'] = area
            curriculum['contact'] = contact
            curriculum['intendedSalary'] = intendedSalary
            curriculum['workload'] = workload

            hasCurriculum = True

            curriculum = ro.register('curriculum', curriculum)

        #2) Alterar dados do currículo
        elif (selectedOption == 2) and (not hasCurriculum):
            print("Você não possui um curriculo cadastrado. Para fazer um cadastro, selecione a opção 1")

        elif (selectedOption == 2) and hasCurriculum:

            curriculumUpdate = { }

            userInput = input("Você gostaria de atualizar seu contato (S/N)? ")
            if userInput.lower() == 's':
                curriculumUpdate['contact'] = input("Contato: ")

            userInput = input("Você gostaria de atualizar seu salário pretendido (S/N)? ")
            if userInput.lower() == 's':
                curriculumUpdate['intendedSalary'] = float(input("Salário pretendido: "))

            userInput = input("Você gostaria de atualizar sua carga horária pretendida (S/N)? ")
            if userInput.lower() == 's':
                curriculumUpdate['workload'] = int(input("Carga horária pretendida: "))

            if(curriculumUpdate):
                curriculumUpdate['id'] = curriculum['id']
                curriculum = ro.update('curriculum', curriculumUpdate) #antes tava ro.update('jobOffer', curriculum)

        #3) Checar vagas cadastradas
        elif (selectedOption == 3):
            #Consulta de vagas de emprego, indicando filtros como área de interesse e salário mínimo pretendido

            jobWanted = { }

            userInput = input("Qual a área procurada? ")
            jobWanted['area'] = userInput

            userInput = float(input("Qual o salário pretendido? "))
            jobWanted['intendedSalary'] = userInput

            if(jobWanted):
                response = ro.get('jobOffer', jobWanted)
                if response["status_code"] == 400:
                    print("Parâmetros de pesquisa incorretos")
                elif response["status_code"] == 404:
                    print("Nenhuma vaga encontrada com os filtros desejados")
                elif response["status_code"] == 200:
                    printJobs(response["body"])
                else:
                    print("A pesquisa retornou erro com status: " + response["status_code"])

if __name__ == "__main__":
    main()
