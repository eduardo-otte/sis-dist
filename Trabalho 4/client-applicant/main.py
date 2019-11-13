import rest_operations as ro
from types import *

url = 'http://localhost:3000'

def main():
    selectedOption = 0
    hasCurriculum = False
    curriculum = { }
    curriculumId = None

    while(selectedOption != 4):
        print("---------------------------")
        print("Selecione a opção desejada:")
        print("1) Enviar currículo")
        print("2) Alterar dados do currículo")
        print("3) Checar vagas cadastradas")
        print("4) Encerrar")

        selectedOption = int(input())

        if (selectedOption == 1) and hasCurriculum:
            print("Você já possui um curriculo cadastrado. Se você quiser atualizar seus dados, selecione a opção 2")

        if (selectedOption == 1) and (not hasCurriculum):

            contact = input("Nome: ")
            area = input("Área de Interesse: ")
            contact = input("Contato: ")
            intendedSalary = float(input("Salário pretendido: "))
            workload = int(input("Carga horária pretendida: "))

            curriculum['name'] = contact
            curriculum['area'] = area
            curriculum['contact'] = contact
            curriculum['intendedSalary'] = intendedSalary
            curriculum['workload'] = workload

            hasCurriculum = True

            ro.register('curriculum', curriculum)

        elif (selectedOption == 2) and (not hasCurriculum):
            print("Você não possui um curriculo cadastrado. Para fazer um cadastro, selecione a opção 1")

        elif (selectedOption == 2) and hasCurriculum:

            curriculum = { }

            userInput = input("Você gostaria de atualizar seu contato (S/N)? ")
            if userInput.lower() == 's':
                curriculum['contact'] = input("Contato: ")

            userInput = input("Você gostaria de atualizar seu salário pretendido (S/N)? ")
            if userInput.lower() == 's':
                curriculum['intendedSalary'] = float(input("Salário pretendido: "))

            userInput = input("Você gostaria de atualizar sua carga horária pretendida (S/N)? ")
            if userInput.lower() == 's':
                curriculum['workload'] = int(input("Carga horária pretendida: "))

            if(curriculum):
                curriculum['id'] = curriculumId
                ro.update('jobOffer', curriculum)

if __name__ == "__main__":
    main()
