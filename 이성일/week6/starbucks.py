import requests

result = requests.post(
    url='https://www.starbucks.co.kr/store/getStore.do?r=Q05F3MLQ18',
    data={
  "r": "EXE5IDZL80",
  "in_biz_cds": "0",
  "in_scodes": "0",
  "ins_lat": "36.5916",
  "ins_lng": "127.2916",
  "search_text": "",
  "p_sido_cd": "04",
  "p_gugun_cd": "0404",
  "in_distance": "0",
  "in_biz_cd": "",
  "isError": "true",
  "iend": "100",
  "searchType": "C",
  "set_date": "",
  "all_store": "0",
  "T03": "0",
  "T01": "0",
  "T27": "0",
  "T12": "0",
  "T09": "0",
  "T30": "0",
  "T05": "0",
  "T22": "0",
  "T21": "0",
  "T36": "0",
  "T43": "0",
  "Z9999": "0",
  "T64": "0",
  "P02": "0",
  "P10": "0",
  "P50": "0",
  "P20": "0",
  "P60": "0",
  "P30": "0",
  "P70": "0",
  "P40": "0",
  "P80": "0",
  "whcroad_yn": "0",
  "P90": "0",
  "P01": "0",
  "new_bool": "0",
  "rndCod": "M421KEHKII"
}
)

data = result.json()

stores = data["list"]

store_names = [store["s_name"] for store in stores]

print(store_names)
