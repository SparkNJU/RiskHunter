import requests

API_KEY = 'your_api_key_here'  # Replace with your actual API key

# Mapping from currency number to currency code
currency_map = {
    1: 'CNY',
    2: 'USD',
    3: 'EUR',
    4: 'JPY',
    5: 'GBP',
    6: 'AUD',
    7: 'HKD',
    8: 'CHF'
}

def get_real_time_exchange_rate(from_currency_number, to_currency_number):
    from_currency = currency_map[from_currency_number]
    to_currency = currency_map[to_currency_number]
    url = f'https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency={from_currency}&to_currency={to_currency}&apikey={API_KEY}'
    response = requests.get(url)
    data = response.json()
    return data

def get_daily_exchange_rate(from_currency_number, to_currency_number):
    from_currency = currency_map[from_currency_number]
    to_currency = currency_map[to_currency_number]
    url = f'https://www.alphavantage.co/query?function=FX_DAILY&from_symbol={from_currency}&to_symbol={to_currency}&apikey={API_KEY}'
    response = requests.get(url)
    data = response.json()
    return data

# Example usage
real_time_data = get_real_time_exchange_rate(1, 2)  # CNY to USD
print(real_time_data)

daily_data = get_daily_exchange_rate(3, 4)  # EUR to JPY
print(daily_data)