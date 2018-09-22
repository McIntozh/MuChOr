# File transfer via Samba

Protocol quailifier is `smb`

## Configuration
| Parameter | Explanation |
| -------- | -------- |
| protocol | The protocol, always `smb`. |
| host | The name or IP of the host where the share is located. |
| share | Name of the share. |
| directory | Optional path to a working directory on the share. If not present, the share itself will be the working directory. |
| domain | The users domain. |
| user | The user name. |
| password | The users password. |

Example:
```
  protocol: smb
  host: host.name
  share: MyShare
  directory: Subfolder1OnMyShare/Subfolder2OnMyShare
  domain: foosystems
  user: foo
  password: bar
```
