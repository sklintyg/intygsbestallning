const anvandare = {
  "hsaId": "TSTNMT2321000156-1079",
    "namn": "Arnold Johansson",
    "currentRole": {
    "name": "LAKARE",
      "desc": "Läkare"
  },
  "authenticationScheme": "urn:inera:webcert:siths:fake",
    "valdVardenhet": {
    "id": "TSTNMT2321000156-1077",
      "namn": "NMT vg3 ve1"
  },
  "valdVardgivare": {
    "id": "TSTNMT2321000156-102Q",
      "namn": "NMT vg3"
  },
  "vardgivare": [
    {
      "id": "IFV1239877878-104TTT1",
      "namn": "WebCert-Vårdgivare1",
      "vardenheter": [
        {
          "id": "IFV1239877878-1042",
          "namn": "WebCert-Enhet1",
          "epost": "enhet1@webcert.invalid.se",
          "postadress": "Storgatan 1",
          "postnummer": "12345",
          "postort": "Småmåla",
          "telefonnummer": "0101234567890",
          "arbetsplatskod": "1234567890",
          "vardgivareOrgnr": "2-orgnr-1041",
          "agandeForm": "OFFENTLIG",
          "vardgivareHsaId": "IFV1239877878-1041",
          "mottagningar": []
        }
      ]
    },
    {
      "id": "IFV1239877878-1043",
      "namn": "WebCert-Vårdgivare2",
      "vardenheter": [
        {
          "id": "IFV1239877878-104TTT2",
          "namn": "WebCert-Enhet2",
          "vardgivareOrgnr": "2-orgnr-1043",
          "agandeForm": "OFFENTLIG",
          "vardgivareHsaId": "IFV1239877878-1043",
          "mottagningar": [
            {
              "id": "IFV1239877878-1046",
              "namn": "WebCert-Enhet2-Mottagning1",
              "postadress": "",
              "postnummer": "12345",
              "postort": "Storgatan 1",
              "telefonnummer": "",
              "arbetsplatskod": "1234567890",
              "agandeForm": "OFFENTLIG",
              "parentHsaId": "IFV1239877878-1045"
            },
            {
              "@class": "se.inera.intyg.infra.integration.hsa.model.Mottagning",
              "id": "IFV1239877878-104C",
              "namn": "WebCert-Enhet2-Mottagning2",
              "postadress": "",
              "postnummer": "12345",
              "postort": "Storgatan 1",
              "telefonnummer": "",
              "arbetsplatskod": "1234567890",
              "agandeForm": "OFFENTLIG",
              "parentHsaId": "IFV1239877878-1045"
            }
          ]
        }
      ]
    }
  ]
}
export default anvandare;
