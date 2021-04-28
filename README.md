# DHBW_Kryptographie

Aufgabe aus dem vierten Semester der DHBW Mosbach, Studiengang Angewandte Informatik im Fach Kryptographie. Dieses Repository darf als Inspiration für nachfolgende Studenten verstanden werden.

Aufgabenstellung ist im Repository zu finden.

## Commands for testing

```
encrypt message "vaccine for covid is stored in building abc" using shift and keyfile shift10.json
decrypt message "fkmmsxo pyb myfsn sc cdybon sx lesvnsxq klm" using shift and keyfile shift10.json
crack encrypted message "fkmmsxo pyb myfsn sc cdybon sx lesvnsxq klm" using shift
```
```
encrypt message "test" using rsa and keyfile rsa32.json
decrypt message "fS0zIA==" using rsa and keyfile rsa32.json
crack encrypted message "fS0zIA" using rsa and keyfile rsa32.json
```
```
encrypt message "vaccine for covid is stored in building abc" using rsa and keyfile rsa1024.json
decrypt message "Xw78+LeF6OyQSCHTRLR7Ytvwsiskta4D1gd0uI1FKkz2xRZN/+K+mi0CxWHaGleMa0t6LnGiosWiVyPQ2aSy/NhyJbUVFRglbLmW+ibQSALGsDkwnKqmMVS/ujVBzP/rZTF2c9pz+hjmFvSHg8q9x1yYUtYR81YIRV4EBOrswCw=" using rsa and keyfile rsa1024.json
crack encrypted message "Xw78+LeF6OyQSCHTRLR7Ytvwsiskta4D1gd0uI1FKkz2xRZN/+K+mi0CxWHaGleMa0t6LnGiosWiVyPQ2aSy/NhyJbUVFRglbLmW+ibQSALGsDkwnKqmMVS/ujVBzP/rZTF2c9pz+hjmFvSHg8q9x1yYUtYR81YIRV4EBOrswCw=" using rsa and keyfile rsa1024.json
```

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

```
intrude channel cpt_syd by msa
send message "vaccine for covid is stored in building abc" from branch_cpt to branch_syd using shift and keyfile shift10.json
send message "vaccine for covid is stored in building abc" from branch_cpt to branch_syd using rsa and keyfile rsa1024.json
```

