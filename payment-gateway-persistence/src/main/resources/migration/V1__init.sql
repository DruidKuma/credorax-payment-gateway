CREATE TABLE user_transactions (
    ut_id               BIGINT  PRIMARY KEY,
    ut_invoice          BIGINT  UNIQUE NOT NULL,
    ut_amount           BIGINT  NOT NULL,
    ut_currency         TEXT    NOT NULL,
    ut_cardholder_name  TEXT    NOT NULL,
    ut_cardholder_email TEXT    NOT NULL,
    ut_card_pan         TEXT    NOT NULL,
    ut_card_expiry      TEXT    NOT NULL
)
