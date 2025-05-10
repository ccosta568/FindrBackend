package util

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys

fun main() {
    val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    val base64Key = Encoders.BASE64.encode(key.encoded)
    println("Generated Base64 Secret Key:\n$base64Key")
}
