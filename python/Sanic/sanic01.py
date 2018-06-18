#!/usr/local/bin/python3

from sanic import Sanic
from sanic.response import text

app = Sanic()


@app.route("/")
async def test(request):
    return text('Hello World!')


if __name__ == "__main__":
    ssl = {'cert': "./keys/server.crt", 'key': "./keys/server.key"}
    app.run(host="0.0.0.0", port=9000, ssl=ssl)
