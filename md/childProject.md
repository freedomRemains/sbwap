---
# 子プロジェクトの配置方法

[TOPに戻る](../README.md)

```
[settings.gradle]

include('sblib')

rootProject.name = 'sbwap'
rootProject.children.each { it.name = rootProject.name + '_' + it.name }
```

- 「settings.gradle」に、記述を追加する。

  - 「include('[取り込む子プロジェクトのフォルダ]')」を記述する。
  - 「rootProject～」により、取り込む子プロジェクトに親プロジェクト名を付与。  
    これにより、複数の親プロジェクトで同じ子プロジェクトを取り込んでも、  
    バッティングしないプロジェクト名とできる。(eclipseなどでも取り込める  
    ようにするために必要な設定)
  
```
[build.gradle]

// ライブラリプロジェクトを使用するための設定
implementation project(':sbwap_sblib')
```

- 「build.gradle」に、記述を追加する。

  - このとき記述するのは「settings.gradle」で設定した親プロジェクト名を  
    プレフィックスとする、バッティングしないプロジェクト名とすること。
