# DHBW_Kryptographie

## Commands for testing

Encryption

- `encrypt message "test" using rsa and keyfile rsa256.json`

Decryption

- `decrypt message "cAsL20KTnZ0m4wz9xqdtaT4fPJUZf2boYYjrpnHep8g=" using rsa and keyfile rsa256.json`

Cracking

- `crack encrypted message "fS0zIA==" using rsa and keyfile rsa32.json`
- `crack encrypted message "cAsL20KTnZ0m4wz9xqdtaT4fPJUZf2boYYjrpnHep8g=" using rsa and keyfile rsa64.json`


Setup

 ```
register participant branch_hkg with type normal
register participant branch_cpt with type normal
register participant branch_sfo with type normal
register participant branch_syd with type normal
register participant branch_wuh with type normal
register participant msa with type intruder

create channel hkg_wuh from branch_hkg to branch_wuh
create channel hkg_cpt from branch_hkg to branch_cpt
create channel cpt_syd from branch_cpt to branch_syd
create channel syd_sfo from branch_syd to branch_sfo
```

crack encrypted message "fS0zIA==" using rsa and keyfile rsa32.json

send message "test" from branch_hkg to branch_cpt using rsa and keyfile rsa256.json

send message "vaccine for covid is stored in building abc" from branch_cpt to branch_syd using rsa and keyfile
rsa1024.json send message "vaccine for covid is stored in building abc" from branch_cpt to branch_syd using shift and
keyfile shift10.json

encrypt message "vaccine for covid is stored in building abc" using shift and keyfile shift3.json crack encrypted
message "ydfflqh iru frylg lv vwruhg lq exloglqj def" using shift