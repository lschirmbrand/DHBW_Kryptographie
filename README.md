# DHBW_Kryptographie

## Commands for testing

Encryption

- `encrypt message "test" using rsa and keyfile rsa256.json`

Decryption

- `decrypt message "cAsL20KTnZ0m4wz9xqdtaT4fPJUZf2boYYjrpnHep8g=" using rsa and keyfile rsa256.json`

Cracking 

- `crack encrypted message "fS0zIA==" using rsa and keyfile rsa32.json`

Register Participants

- `register participant branch_hkg with type normal`
- `register participant branch_cpt with type normal`
- `register participant branch_sfo with type normal`
- `register participant branch_syd with type normal`
- `register participant branch_wuh with type normal`
- `register participant msa with type intruder`

Create Channels

- `create channel hkg_wuh from branch_hkg to branch_wuh`
- `create channel hkg_cpt from branch_hkg to branch_cpt`
- `create channel cpt_syd from branch_cpt to branch_syd`
- `create channel syd_sfo from branch_syd to branch_sfo`


crack encrypted message "fS0zIA==" using rsa and keyfile rsa32.json