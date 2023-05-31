# CM Project

Designed for single image reconstruction.

### Endpoints

|     Endpoints     | Input type  | Output type | Purpose                          |
|:-----------------:|:-----------:|:-----------:|----------------------------------|
|  `/image/edges`   | PNG or JPEG |     PNG     | Trimmed edge detection           |
| `/image/vertices` | PNG or JPEG |     PNG     | Vertices detection               |
| `/object/convert` | PNG or JPEG |     OBJ     | SfS depth map to .obj conversion |

### Request example

```
curl -k -v --http2 -F "img=@path/to/file" https://localhost:8443/image/edges --output "path/to/save/file"
```

### Generating keystore example

```
RUN keytool -genkeypair -alias localhost_ssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.jks -validity 365 -storepass password -keypass password -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown"
```
