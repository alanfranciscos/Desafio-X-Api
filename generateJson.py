import random
import string
import json

def generate_random_string(length):
    letters = string.ascii_letters
    return ''.join(random.choice(letters) for _ in range(length))

def generate_random_cnpj():
    cnpj = ''.join(random.choice(string.digits) for _ in range(14))
    return f"{cnpj[:2]}.{cnpj[2:5]}.{cnpj[5:8]}/{cnpj[8:12]}-{cnpj[12:14]}"

def generate_random_email():
    username = generate_random_string(8)
    domain = generate_random_string(5)
    return f"{username}@{domain}.com"

def generate_random_phone():
    phone = ''.join(random.choice(string.digits) for _ in range(10))
    return f"({phone[:2]}) {phone[2:6]}-{phone[6:]}"

def generate_random_state():
    states = ['SP', 'RJ', 'MG', 'RS', 'BA', 'PR', 'SC', 'GO', 'AM', 'PE']
    return random.choice(states)

def generate_random_location():
    x = round(random.uniform(-90, 90), 4)
    y = round(random.uniform(-180, 180), 4)
    return {"x": x, "y": y}

def generate_random_record():
    record = {
        "nome": generate_random_string(10),
        "cnpj": generate_random_cnpj(),
        "email": generate_random_email(),
        "telefone": generate_random_phone(),
        "estado": generate_random_state(),
        "location": generate_random_location()
    }
    return record

# Generate a random record
random_record = generate_random_record()

# Convert the record to JSON
json_record = json.dumps(random_record, indent=4)

# Print the JSON record
print(json_record)
