jarsigner -keystore components/rsaBase/keystore.jks -storepass dhbw2021 components/rsaBase/build/libs/RSABase.jar server
jarsigner -keystore components/rsaCracker/keystore.jks -storepass dhbw2021 components/rsaCracker/build/libs/RSACracker.jar server
jarsigner -keystore components/shiftBase/keystore.jks -storepass dhbw2021 components/shiftBase/build/libs/ShiftBase.jar server
jarsigner -keystore components/shiftCracker/keystore.jks -storepass dhbw2021 components/shiftCracker/build/libs/ShiftCracker.jar server