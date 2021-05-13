/*
 * The MIT License (MIT)
 * Copyright © 2020 NBCO YooMoney LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.yoomoney.sdk.kassa.payments.model

import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType

internal sealed class PaymentOption {
    abstract val id: Int
    abstract val charge: Amount
    abstract val fee: Fee?
    abstract val savePaymentMethodAllowed: Boolean
}

internal data class GooglePay(
    override val id: Int,
    override val charge: Amount,
    override val fee: Fee?,
    override val savePaymentMethodAllowed: Boolean
) : PaymentOption()

internal data class NewCard(
    override val id: Int,
    override val charge: Amount,
    override val fee: Fee?,
    override val savePaymentMethodAllowed: Boolean
) : PaymentOption()

internal sealed class YooMoney : PaymentOption()

internal data class Wallet(
    override val id: Int,
    override val charge: Amount,
    override val fee: Fee?,
    val walletId: String,
    val balance: Amount,
    override val savePaymentMethodAllowed: Boolean
) : YooMoney()

internal data class AbstractWallet(
    override val id: Int,
    override val charge: Amount,
    override val fee: Fee?,
    override val savePaymentMethodAllowed: Boolean
) : YooMoney()

internal data class LinkedCard(
    override val id: Int,
    override val charge: Amount,
    override val fee: Fee?,
    val cardId: String,
    val brand: CardBrand,
    val pan: String,
    val name: String? = null,
    val isLinkedToWallet: Boolean = false,
    override val savePaymentMethodAllowed: Boolean
) : YooMoney()

internal data class SbolSmsInvoicing(
    override val id: Int,
    override val charge: Amount,
    override val fee: Fee?,
    override val savePaymentMethodAllowed: Boolean
) : PaymentOption()

internal data class PaymentIdCscConfirmation(
    override val id: Int,
    override val charge: Amount,
    override val fee: Fee?,
    val paymentMethodId: String,
    val first: String,
    val last: String,
    val expiryYear: String,
    val expiryMonth: String,
    override val savePaymentMethodAllowed: Boolean
) : PaymentOption()

internal fun PaymentOption.toType() = when (this) {
    is NewCard -> PaymentMethodType.BANK_CARD
    is YooMoney -> PaymentMethodType.YOO_MONEY
    is SbolSmsInvoicing -> PaymentMethodType.SBERBANK
    is GooglePay -> PaymentMethodType.GOOGLE_PAY
    is PaymentIdCscConfirmation -> PaymentMethodType.BANK_CARD
}