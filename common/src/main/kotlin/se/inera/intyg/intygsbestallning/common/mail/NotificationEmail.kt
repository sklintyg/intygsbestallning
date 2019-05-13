/**
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsbestallning.common.mail

import se.inera.intyg.intygsbestallning.common.text.mail.MailTexter

data class NotificationEmail(
   var toAddress: String? = null,
   var subject: String? = null,
   var body: String? = null
)

data class MailContent(
   val hostUrl: String,
   val texter: MailTexter,
   val url: String
) {
  companion object Template {
    fun toText(data: MailContent): String {
      return data.texter.halsning.text +
         "\n\r\n\r${data.texter.body.text1}\n\r${data.url}\n\r${data.texter.body.text2}" +
         "\n\r\n\r" + "${data.texter.footer.text1}\n\r${data.texter.footer.text2}"
    }

    @Suppress("LongMethod")
    fun toHtml(data: MailContent): String {
      return """
        <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        <html>
         <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
            <title>FMU-Notifiering</title>
            <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
            <style type="text/css">
                body {
                    margin:0;
                    padding:10px;
                    font-family: 'Roboto', helvetica, sans-serif;
                    font-size: 11pt;
                    font-weight: normal;

                }
                img{border:0; height:auto; line-height:100%; outline:none; text-decoration:none;}
                table td{border-collapse:collapse;}
                #backgroundTable{height:100% !important; margin:0; padding:0; width:100% !important;}
                #templateFooter {
                    font-size: 10pt;
                    font-weight: lighter;
                    font-style: italic;
                    color: #A9B2BD;
                }
                h1 { font-size: 11pt; font-weight: bold;  }
            </style>
            <!--[if (gte mso 9)|(IE)]><style type="text/css">
            body, table, td, a, p {font-family: Arial, sans-serif !important; font-size: 11px; font-weight: 400;}
            </style><![endif]-->
         </head>
        <body leftmargin="0" marginwidth="0" topmargin="0" marginheight="0" offset="0">
            <center>
                <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%" id="backgroundTable">
                    <tr>
                        <td align="center" valign="top">
                            <table border="0" cellpadding="0" cellspacing="0" width="600">
                                <tr>
                                    <td align="left" valign="top">

                                            <h1>${data.texter.halsning.text}</h1>
                                            <p>${data.texter.body.text1}</p>

                                            <p>
                                                <a href="${data.url}">${data.url}</a>
                                            </p>

                                            <p>${data.texter.body.text2}</p
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" valign="top" id="templateFooter">
                                        <br>
                                          <img alt="Intygsbeställning logo"
                                               src="${data.hostUrl}/images/${data.texter.logo}"
                                               height="30"
                                               width="221">
                                        <br>
                                        <p>${data.texter.footer.text1}</p>
                                        <p>${data.texter.footer.text2}</p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </center>
        </body>
        </html>
      """
    }
  }
}
