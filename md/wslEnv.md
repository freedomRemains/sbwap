---
# WSL環境の構築
- WindowsPowerShellにて、次のコマンドを実行する。
```
wsl --install
```
- インストールできない場合は、chatGPTやGitHub copilotといったAIに聞く。  
「WSL インストール」などで地道にGoogle検索しても出てくる。  
たいていはコントロールパネルで「Windowsの機能の有効化または無効化」でWSLを有効にすれば解決できる。  
まれにPC起動時のBIOS設定変更が必要なことがあるが、2025年1月時点での一般的なPCではまずその問題は起きない。
- 次のコマンドを実行し、WSLのバージョンが2系(もしくはそれ以上)であることを確認する。
```
wsl -l -v
```
- WSLが2系でない場合は、次のコマンドを実行する。  
時間経過により「2」のところは「3」とか「4」になるかも知れない。  
AIに聞くかGoogle検索で最新情報を確認し、適宜コマンドは見直す。
```
wsl --set-default-version 2
```
- 次のコマンドでWSLを最新化する。
```
wsl --update
```
- 次のコマンドでWSL環境にUbuntuをインストールする。  
インストール時にはユーザ名とパスワードを聞かれるので準備しておくこと。  
パスワードはポリシーの制約があるので、あまり単純なものはダメ。
```
wsl --install Ubuntu
```
- 別のWindowsPowerShellを起動して次のコマンドを実行し、シャットダウンと起動ができることを確認する。
```
wsl --shutdown
wsl -d Ubuntu
```
- Ubuntu環境にdocker及びdocker composeをインストールする。  
このコマンドはdockerの本家サイトに掲載されており、AIなどに聞いても分かる。  
情報は常に陳腐化する可能性があるので、うまくいかないときは最新のコマンドを調べること。
```
sudo apt update
sudo apt upgrade -y
sudo apt install curl -y
sudo apt install apt-transport-https -y
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io -y
sudo apt install docker-compose -y
sudo service docker start
sudo usermod -aG docker $USER
sudo systemctl enable docker
sudo systemctl status docker
```
- 次のコマンドを実行し、docker及びdocker composeがインストールできているか確認する。
```
docker --version
docker compose version
```
- WSL上にUbuntuを作成すると、エクスプローラでルート以下のディレクトリ構成にアクセスできる。  
/home配下など、適切な場所にdocker-compose.ymlを配置する。
- 次のようなコマンドでdocker composeを起動する。
```
cd [docker-compose.ymlの配置先ディレクトリ]
docker compose up -d
＜例＞
cd /home/develop/tool/docker
docker compose up -d
```
