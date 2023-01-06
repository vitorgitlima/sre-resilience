print('Start #################################################################');
db = db.getSiblingDB('opin-db-apis')

db.createCollection('faixa_cep');
db.createCollection('produtos');

print('Insert data into faixa-cep #################################################################');
db.getCollection('faixa_cep').insert({
	"rangeId" : "363",
	"versionCode" : "4",
	"productCode" : "81032183",
	"regionCode" : "1",
	"postalCodeStart" : "00000100",
	"postalCodeEnd" : "99999999",
	"region" : "RM S?o Paulo",
	"federateUnit" : "SP",
	"changeByUser" : "7567958",
	"lastDateModified" : "2019-11-18 16:28:22.957"
});

db.getCollection('faixa_cep').insert({
	"rangeId" : "363",
	"versionCode" : "4",
	"productCode" : "92739011",
	"regionCode" : "1",
	"postalCodeStart" : "00000000",
	"postalCodeEnd" : "00000089",
	"region" : "RM S?o Paulo",
	"federateUnit" : "SP",
	"changeByUser" : "7567958",
	"lastDateModified" : "2019-11-18 16:28:22.957"
});

db.getCollection('faixa_cep').insert({
	"rangeId" : "363",
	"versionCode" : "4",
	"productCode" : "92737125",
	"regionCode" : "1",
	"postalCodeStart" : "00000100",
	"postalCodeEnd" : "99999999",
	"region" : "RM S?o Paulo",
	"federateUnit" : "SP",
	"changeByUser" : "7567958",
	"lastDateModified" : "2019-11-18 16:28:22.957"
});

db.getCollection('faixa_cep').insert({
	"rangeId" : "363",
	"versionCode" : "4",
	"productCode" : "92740187",
	"regionCode" : "1",
	"postalCodeStart" : "00000100",
	"postalCodeEnd" : "99999999",
	"region" : "RM S?o Paulo",
	"federateUnit" : "SP",
	"changeByUser" : "7567958",
	"lastDateModified" : "2019-11-18 16:28:22.957"
});

db.getCollection('faixa_cep').insert({
	"rangeId" : "363",
	"versionCode" : "4",
	"productCode" : "92530186",
	"regionCode" : "1",
	"postalCodeStart" : "00000100",
	"postalCodeEnd" : "99999999",
	"region" : "RM S?o Paulo",
	"federateUnit" : "SP",
	"changeByUser" : "7567958",
	"lastDateModified" : "2019-11-18 16:28:22.957"
});

db.getCollection('faixa_cep').insert({
	"rangeId" : "363",
	"versionCode" : "4",
	"productCode" : "92742009",
	"regionCode" : "1",
	"postalCodeStart" : "00000100",
	"postalCodeEnd" : "99999999",
	"region" : "RM S?o Paulo",
	"federateUnit" : "SP",
	"changeByUser" : "7567958",
	"lastDateModified" : "2019-11-18 16:28:22.957"
});

print('Insert data into produtos #################################################################');
db.getCollection('produtos').insert({
	"brandName" : "Bradesco Seguros S.A.",
	"companyName" : "Bradesco Auto e RE",
	"cnpjNumber" : "92682038000100",
	"product" : {
		"name" : "Bradesco Bilhete Residencial",
		"code" : "81032183",
		"propertyCharacteristics" : [
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "PREDIO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "MISTA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "CONTEUDO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "METALICA",
				"propertyUsageType" : "DESOCUPADO",
				"destinationInsuredImportance" : "PREDIO"
			},
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "AMBOS"
			}
		],
		"protective" : false,
		"customerServices" : ["REDE_REFERENCIADA"],
		"premiumPayments" : [
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "CARTAO_CREDITO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PARCELADO"
			},
			{
				"paymentMethod" : "PIX",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PARCELADO"
			}
		],
		"targetAudiences" : ["PESSOA_JURIDICA"],
		"additional" : ["SORTEIO_GRATUITO"],
        "additionalOthers" : "<VERIFICAR COM PO - Não existe na planilha>",
		"premiumRates" : ["NID"],
		"minimumRequirements": [
		    {
			    "contractingType" : "AMBAS",
			    "contractingMinRequirement" : "NID"
		    }
        ],
		"termsAndConditions" : [
			{
				"susepProcessNumber" : "15414.005043/2005-18",
				"definition" : "NID"
			}
		],
        "validity" : [
            {
                "term" : "ANUAL",
                "termOthers" : "others"
            }
        ],
		"assistanceServices" : [
			{
				"assistanceServicesPackage" : "ATE_10_SERVICOS",
				"assistanceServicesDetail" : "NID",
				"chargeTypeSignaling" : "GRATUITA"
			}
		],
		"coverages" : [
			{
				"coverageType" : "ROUBO_SUBTRACAO_BENS",
                "coverageDetail" : "detail",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 1000,
						"unit" : {
							"code" : "R$",
							"description" : "real"
						}
					},
					"maxLMI" : {
						"amount" : 1000,
						"unit" : {
							"code" : "R$",
							"description" : "real"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 1000,
						"unit" : {
							"code" : "R$",
							"description" : "real"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			}
		]
	}
});

db.getCollection('produtos').insert({
	"brandName" : "Bradesco Seguros S.A.",
	"companyName" : "Bradesco Auto e RE",
	"cnpjNumber" : "92682038000100",
	"product" : {
		"name" : "Bradesco Seguro Residencial Classic",
		"code" : "92737125",
		"propertyCharacteristics" : [
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType": "HABITUAL",
                "destinationInsuredImportance": "PREDIO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "MISTA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "CONTEUDO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "METALICA",
				"propertyUsageType" : "DESOCUPADO",
				"destinationInsuredImportance" : "PREDIO"
			},
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "AMBOS"
			}
		],
		"protective" : false,
		"customerServices" : ["REDE_REFERENCIADA"],
		"premiumPayments" : [
			{
				"paymentMethod" : "PONTOS_PROGRAMAS_BENEFICIO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "CARTAO_CREDITO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PARCELADO"
			},
			{
				"paymentMethod" : "DEBITO_CONTA_CORRENTE",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PARCELADO"
			}
		],
		"targetAudiences" : ["PESSOA_NATURAL"],
		"additional" : ["SORTEIO_GRATUITO"],
        "additionalOthers" : "<VERIFICAR COM PO - Não existe na planilha>",
		"premiumRates" : ["NID"],
		"minimumRequirements" : [
		    {
			    "contractingType" : "COLETIVO",
			    "contractingMinRequirement" : "NID"
		    }
        ],
		"termsAndConditions" : [
			{
				"susepProcessNumber" : "15414.005043/2005-18",
				"definition" : "NID"
			}
		],
        "validity" : [
            {
                "term" : "ANUAL",
                "termOthers" : "others"
            }
        ],
		"assistanceServices" : [
			{
				"assistanceServicesPackage" : "ACIMA_20_SERVICOS",
				"assistanceServicesDetail": "detail",
				"chargeTypeSignaling" : "GRATUITA"
			}
		],
		"coverages" : [
			{
				"coverageType" : "ROUBO_SUBTRACAO_BENS",
                "coverageDetail" : "NID",
                "coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 20000,
						"unit" : {
							"code" : "R$",
							"description" : "real"
						}
					},
					"maxLMI" : {
						"amount" : 20000,
						"unit" : {
							"code" : "R$",
							"description" : "real"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 20000,
						"unit" : {
							"code" : "R$",
							"description" : "real"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			}
		]
	}
});

db.getCollection('produtos').insert({
	"brandName" : "Bradesco Seguros S.A.",
	"companyName" : "Bradesco Auto e RE",
	"cnpjNumber" : "92682038000100",
	"product" : {
		"name" : "Bradesco Seguro Residencial Exclusive",
		"code" : "92740187",
		"propertyCharacteristics" : [
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "MADEIRA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "PREDIO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "MISTA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "CONTEUDO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType" : "DESOCUPADO",
				"destinationInsuredImportance" : "PREDIO"
			},
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "AMBOS"
			}
		],
		"protective" : false,
		"customerServices" : ["REDE_REFERENCIADA"],
		"premiumPayments" : [
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PARCELADO"
			},
			{
				"paymentMethod" : "CARTAO_CREDITO",
				"paymentMethodDetail" : "detail",
				"paymentType" : "PARCELADO"
			}
		],
		"targetAudiences" : ["AMBAS"],
		"additional" : ["SORTEIO_GRATUITO"],
        "additionalOthers" : "<VERIFICAR COM PO - Não existe na planilha>",
		"premiumRates" : ["NID"],
		"minimumRequirements" : [
		    {
			    "contractingType" : "INDIVIDUAL",
			    "contractingMinRequirement" : "NID"
		    }
        ],
		"termsAndConditions" : [
			{
				"susepProcessNumber" : "15414.005043/2005-18",
				"definition" : "NID"
			}
		],
        "validity" : [
            {
                "term" : "ANUAL",
                "termOthers" : "<Verificar com PO - Não existe na planilha>"
            }
        ],
		"assistanceServices" : [
			{
				"assistanceServicesPackage" : "ACIMA_20_SERVICOS",
				"assistanceServicesDetail" : "NID",
                "chargeTypeSignaling" : "GRATUITA"
			}
		],
		"coverages" : [
			{
				"coverageType" : "ROUBO_SUBTRACAO_BENS",
                "coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 3000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 3000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 3000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "RESPONSABILIDADE_CIVIL_FAMILIAR",
                "coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 40000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 40000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 40000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "ALAGAMENTO",
                "coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 50000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
					},
					"minDeductibleAmount" : {
						"amount" : 50000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "VENDAVAL",
                "coverageDetail" : "detail",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
                    "insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "ESCRITORIO_RESIDENCIA",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "TERREMOTO",
				"coverageDetail" : "NID",
                "coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "IMPACTO_VEICULOS",
                "coverageDetail" : "NID",
                "coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "ROUBO_SUBTRACAO_BENS",
                "coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "DANOS_POR_AGUA",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "TERREMOTO",
                "coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                   "minLMI" : {
                       "amount" : 60000,
                       "unit" : {
                           "code" : "R$",
                           "description" : "REAL"
                       }
                   },
                   "maxLMI" : {
                       "amount" : 60000,
                       "unit" : {
                           "code" : "R$",
                           "description" : "REAL"
                       }
                   },
                   "minDeductibleAmount" : {
                       "amount" : 60000,
                       "unit" : {
                           "code" : "R$",
                           "description" : "REAL"
                       }
                   },
                   "insuredMandatoryParticipationPercentage" : 0
               }
			},
			{
				"coverageType" : "OUTRAS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                      "minLMI" : {
                          "amount" : 60000,
                          "unit" : {
                              "code" : "R$",
                              "description" : "REAL"
                          }
                      },
                      "maxLMI" : {
                          "amount" : 60000,
                          "unit" : {
                              "code" : "R$",
                              "description" : "REAL"
                          }
                      },
                      "minDeductibleAmount" : {
                          "amount" : 60000,
                          "unit" : {
                              "code" : "R$",
                              "description" : "REAL"
                          }
                      },
                      "insuredMandatoryParticipationPercentage" : 0
                  }
			},
			{
				"coverageType" : "IMPACTO_AERONAVES",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                     "minLMI" : {
                         "amount" : 60000,
                         "unit" : {
                             "code" : "R$",
                             "description" : "REAL"
                         }
                     },
                     "maxLMI" : {
                         "amount" : 60000,
                         "unit" : {
                             "code" : "R$",
                             "description" : "REAL"
                         }
                     },
                     "minDeductibleAmount" : {
                         "amount" : 60000,
                         "unit" : {
                             "code" : "R$",
                             "description" : "REAL"
                         }
                     },
                     "insuredMandatoryParticipationPercentage" : 0
                 }
			},
			{
				"coverageType" : "MICROEMPREENDEDOR",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "DANOS_ELETRICOS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                   "minLMI" : {
                       "amount" : 60000,
                       "unit" : {
                           "code" : "R$",
                           "description" : "REAL"
                       }
                   },
                   "maxLMI" : {
                       "amount" : 60000,
                       "unit" : {
                           "code" : "R$",
                           "description" : "REAL"
                       }
                   },
                   "minDeductibleAmount" : {
                       "amount" : 60000,
                       "unit" : {
                           "code" : "R$",
                           "description" : "REAL"
                       }
                   },
                   "insuredMandatoryParticipationPercentage" : 0
               }
			},
			{
				"coverageType" : "DANOS_POR_AGUA",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                       "minLMI" : {
                           "amount" : 60000,
                           "unit" : {
                               "code" : "R$",
                               "description" : "REAL"
                           }
                       },
                       "maxLMI" : {
                           "amount" : 60000,
                           "unit" : {
                               "code" : "R$",
                               "description" : "REAL"
                           }
                       },
                       "minDeductibleAmount" : {
                           "amount" : 60000,
                           "unit" : {
                               "code" : "R$",
                               "description" : "REAL"
                           }
                       },
                       "insuredMandatoryParticipationPercentage" : 0
                   }
			}
		]
	}
});

db.getCollection('produtos').insert({
	"brandName" : "Bradesco Seguros S.A.",
	"companyName" : "Bradesco Auto e RE",
	"cnpjNumber" : "92682038000100",
	"product" : {
		"name" : "Bradesco Seguro Residencial Prime",
		"code" : "92530186",
		"propertyCharacteristics" : [
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "MADEIRA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "PREDIO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "MISTA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "CONTEUDO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType" : "DESOCUPADO",
				"destinationInsuredImportance" : "PREDIO"
			},
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "AMBOS"
			}
		],
		"protective" : false,
		"customerServices" : ["REDE_REFERENCIADA"],
		"premiumPayments" : [
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "VERIFICAR COM PO - Não existe na planilha",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "VERIFICAR COM PO - Não existe na planilha",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "VERIFICAR COM PO - Não existe na planilha",
				"paymentType" : "PARCELADO"
			},
			{
				"paymentMethod" : "CARTAO_CREDITO",
				"paymentMethodDetail" : "VERIFICAR COM PO - Não existe na planilha",
				"paymentType" : "PARCELADO"
			}
		],
		"targetAudiences" : ["PESSOA_NATURAL", "AMBAS"],
		"additional" : ["SORTEIO_GRATUITO"],
		"additionalOthers" : "<VERIFICAR COM PO - Não existe na planilha>",
		"premiumRates" : ["NID"],
		"minimumRequirements" : [
		    {
			    "contractingType" : "INDIVIDUAL",
			    "contractingMinRequirement" : "NID"
		    }
        ],
		"termsAndConditions" : [
			{
				"susepProcessNumber" : "15414.005043/2005-18",
				"definition" : "NID"
			}
		],
		"validity" : [
			{
				"term" : "ANUAL",
				"termOthers" : "<Verificar com PO - Não existe na planilha>"
			}
		],
		"assistanceServices" : [
			{
				"assistanceServicesPackage" : "ACIMA_20_SERVICOS",
				"assistanceServicesDetail" : "NID",
                "chargeTypeSignaling" : "PAGO"
			}
		],
		"coverages" : [
			{
				"coverageType" : "ROUBO_SUBTRACAO_BENS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 3000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 3000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 3000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				},
			},
			{
				"coverageType" : "RESPONSABILIDADE_CIVIL_FAMILIAR",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 40000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 40000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 40000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				},
			},
			{
				"coverageType" : "ALAGAMENTO",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				},
			},
			{
				"coverageType" : "VENDAVAL",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				},
			},
			{
				"coverageType" : "INCENDIO",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				},
			},
			{
				"coverageType" : "TERREMOTO",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "IMPACTO_VEICULOS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "IMPACTO_AERONAVES",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "DANOS_POR_AGUA",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "TERREMOTO",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "OUTRAS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "IMPACTO_AERONAVES",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "MICROEMPREENDEDOR",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "DANOS_ELETRICOS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"minDeductibleAmount" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "DANOS_POR_AGUA",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			}
		]
	}
});

db.getCollection('produtos').insert({
	"brandName" : "Bradesco Seguros S.A.",
	"companyName" : "Bradesco Auto e RE",
	"cnpjNumber" : "92682038000100",
	"product" : {
		"name" : "Bradesco Seguro Residencial Sob Medida",
		"code" : "92742009",
		"propertyCharacteristics" : [
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "MADEIRA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "PREDIO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "MISTA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "CONTEUDO"
			},
			{
				"propertyType" : "CASA",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType" : "DESOCUPADO",
				"destinationInsuredImportance" : "PREDIO"
			},
			{
				"propertyType" : "APARTAMENTO",
				"propertyBuildType" : "ALVENARIA",
				"propertyUsageType" : "HABITUAL",
				"destinationInsuredImportance" : "AMBOS"
			}
		],
		"protective" : false,
		"customerServices" : ["REDE_REFERENCIADA"],
		"premiumPayments" : [
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "VERIFICAR COM PO - Não existe na planilha",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "VERIFICAR COM PO - Não existe na planilha",
				"paymentType" : "PAGAMENTO_UNICO"
			},
			{
				"paymentMethod" : "BOLETO_BANCARIO",
				"paymentMethodDetail" : "VERIFICAR COM PO - Não existe na planilha",
				"paymentType" : "PARCELADO"
			},
			{
				"paymentMethod" : "CARTAO_CREDITO",
				"paymentMethodDetail" : "VERIFICAR COM PO - Não existe na planilha",
				"paymentType" : "PARCELADO"
			}
		],
		"targetAudiences" : ["PESSOA_NATURAL", "PESSOA_JURIDICA"],
		"additional" : ["SORTEIO_GRATUITO"],
		"additionalOthers" : "<VERIFICAR COM PO - Não existe na planilha>",
		"premiumRates" : ["NID"],
		"minimumRequirements" : [
		    {
			    "contractingType" : "INDIVIDUAL",
			    "contractingMinRequirement" : "NID"
		    }
        ],
		"termsAndConditions" : [
			{
				"susepProcessNumber" : "15414.005043/2005-18",
				"definition" : "NID"
			}
		],
        "validity" : [
            {
                "term" : "ANUAL",
                "termOthers" : "<Verificar com PO - Não existe na planilha>"
            }
        ],
		"assistanceServices" : [
			{
				"assistanceServicesPackage" : "ACIMA_20_SERVICOS",
				"assistanceServicesDetail" : "NID",
				"chargeTypeSignaling" : "GRATUITA"
			}
		],
		"coverages" : [
			{
				"coverageType" : "ROUBO_SUBTRACAO_BENS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 3000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
                        "amount" : 3000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
					"minDeductibleAmount" : {
                        "amount" : 3000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "RESPONSABILIDADE_CIVIL_FAMILIAR",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 40000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
                        "amount" : 40000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
					"minDeductibleAmount" : {
                        "amount" : 40000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "ALAGAMENTO",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 50000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
                        "amount" : 50000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
					"minDeductibleAmount" : {
                        "amount" : 50000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "VENDAVAL",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 50000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 50000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 50000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "TERREMOTO",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
					"minLMI" : {
						"amount" : 60000,
						"unit" : {
							"code" : "R$",
							"description" : "REAL"
						}
					},
					"maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
					"minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
					"insuredMandatoryParticipationPercentage" : 0
				}
			},
			{
				"coverageType" : "TERREMOTO",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
				"coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "IMPACTO_VEICULOS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
                "coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "JOIAS_OBRAS_ARTE",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
                "coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "DANOS_POR_AGUA",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
                "coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "TERREMOTO",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
                "coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "OUTRAS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
                "coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "IMPACTO_AERONAVES",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
                "coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "MICROEMPREENDEDOR",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
                "coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "DANOS_ELETRICOS",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
                "coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			},
			{
				"coverageType" : "DANOS_POR_AGUA",
				"coverageDetail" : "NID",
				"coveragePermissionSeparateAcquisition" : false,
                "coverageAttributes" : {
                    "minLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "maxLMI" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "minDeductibleAmount" : {
                        "amount" : 60000,
                        "unit" : {
                            "code" : "R$",
                            "description" : "REAL"
                        }
                    },
                    "insuredMandatoryParticipationPercentage" : 0
                }
			}
		]
	}
});

print('END #################################################################');