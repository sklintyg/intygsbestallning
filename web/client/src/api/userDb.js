const userDb = {
  "hsaId": "TSTNMT2321000156-1079",
    "namn": "Arnold Johansson",
    "currentRole": {
    "name": "LAKARE",
      "desc": "Läkare"
  },
  "authenticationScheme": "urn:inera:webcert:siths:fake",
  "valdVardenhet": {
  "id": "ve1",
    "namn": "Vårdenhet 1"
  },
  "valdVardgivare": {
    "id": "vg1",
      "namn": "Vårdgivare 1"
  },
  "vardgivare": [
    {
      "id": "vg1",
      "namn": "Vårdgivare 1",
      "vardenheter": [
        {
          "id": "ve1",
          "namn": "Vårdenhet 1",
          "epost": "enhet1@webcert.invalid.se",
          "postadress": "Storgatan 1",
          "postnummer": "12345",
          "postort": "Småmåla",
          "telefonnummer": "0101234567890",
          "arbetsplatskod": "1234567890",
          "vardgivareOrgnr": "2-orgnr-1041",
          "agandeForm": "OFFENTLIG",
          "vardgivareHsaId": "vg1",
          "mottagningar": []
        }
      ]
    },
    {
      "id": "vg2",
      "namn": "Vårdgivare 2",
      "vardenheter": [
        {
          "id": "ve2",
          "namn": "Vårdenhet 2",
          "vardgivareOrgnr": "2-orgnr-1043",
          "agandeForm": "OFFENTLIG",
          "vardgivareHsaId": "vg2",
          "mottagningar": [
            {
              "id": "IFV1239877878-1046",
              "namn": "WebCert-Enhet2-Mottagning1",
              "postadress": "",
              "postnummer": "12345",
              "postort": "Storgatan 1",
              "telefonnummer": "",
              "arbetsplatskod": "1234567890",
              "agandeForm": "OFFENTLIG"
            }
          ]
        },
        {
          "id": "ve3",
          "namn": "Vårdenhet 3",
          "agandeForm": "OFFENTLIG",
          "vardgivareHsaId": "vg2",
          "mottagningar": []
        }
      ]
    }
  ]
}
export default userDb;
