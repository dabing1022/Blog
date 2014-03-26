# 详解利用ShoeBox制作位图字体
======================================

## 1 ShoeBox 简介

![](https://s2uzig.blu.livefilestore.com/y2pStZvJ2Cq8GkU4Tjt-CSwgKOOfAouep_m6NmfURLDktE7uNWycfWQUEU296naf41sQSzKQNGgxJKHbSHBoh8VK4ZgRAYUxamUss72SqdS6Sk/screenshot_shoebox3.jpg?psid=1)

-  [ShoeBox官网](http://renderhjs.net/shoebox/)

ShoeBox是一个基于AdobeAIR实现的免费跨平台的工具。这个工具使用拖放、剪切板的工作流程方式，能够很方便的处理游戏图片、创建位图字体等。

-  支持引擎

![](https://s2uzig.blu.livefilestore.com/y2pPObcqxJ-WEfCGII2NoAQx7q5t2rQB_rr7Lppe4v6LLU3T0diT-OxiLgLeI8jOwOM1QiDY2j2Z1dNtPUe_yeo03WsPz5OQ1J4jDdWSV3QQ3A/support.png?psid=1)

## 2 功能概括介绍

ShoeBox虽小，五脏俱全。作者做这个工具足见是用了心，如果各位想捐款，官网右上角:)。写这篇文字的时候，笔者用的版本是最新版3.4.2。官网的一些图片说明不少是老版本的，注意下就可以了。

### Sprites

-  打包位图（Sprite Sheet）

![](https://s2uzig.blu.livefilestore.com/y2pXd3vpL871b__ttajFp3X0LuSnjS6nxZJJ0poAIHve_gBoeYQvaxx9q-o_bNhaHg-t23ao2Mtt3AASDs9veQ06Ah51wopwPgBka7RZyv1nxM/screenshot_spritePack.jpg?psid=1)

这个功能非常强大。我们在游戏制作中，经常利用TexturePacker等工具，将一些碎图图片合成一个精灵表单(spritesheet)，好处不言而喻。ShoeBox的这个功能，在一些方面并没有TP工具那么强大，但却有自己的特色功能。TA可以将多个图片，SWF(AS1,AS2,AS3版本)动画或者GIF动画合成到一个纹理图上，强大吧，支持SWF/GIF哦。

-  读取精灵（Read Sprites）

![](https://s2uzig.blu.livefilestore.com/y2pT9d4V1YvasgB-_gtd9wm-rkwDgbMIe4pe2PM9lZVP2tTcV73JQCFvMey5vddEyte3eDGDDUjkwpTxfD9ylmljqE1hWdaQWIc5SEKEgBUu1s/example_readSpriteSheet_sprites.jpg?psid=1)

读取一个精灵表单或者位图字体集，并将他们单独导出为精灵图片。要求图片文件和查找索引文本文件（即图片描述数据文件）。

-  提取精灵（Extract Sprites）

![](https://s2uzig.blu.livefilestore.com/y2pbRIjUGfoQBF72T7esa5M4JF8r2nPwS3crKIN7vrbfdgKqEwbQMpz5lNApqz8n8nb3zp0-Iq07ZNj3jrjWKg9E0Zei127XrcHeZSdntsc5-g/extract_sprites.png?psid=1)

上图是我拿`FlappyBird`的精灵表单做的试验。

检测具有alpha通道的图片的精灵图片并一次标上序号导出。这个功能可以在你丢失了图片描述文件的情况下，用作精灵表单反向导出精灵图片。同一个物体如果在图片上有透明间隙，会被表上多个序号，这里不是很理想，朋友们如果要用这个功能的话注意下。

-  设定锚点（Sprite Pivots）

![](https://s2uzig.blu.livefilestore.com/y2pgofMoTbt_w6_qqArB03K11Gm2TToycNwrpHOd3I6dQpOqj_T_wOi5Io4fffQgkHuntQMyX9AG0cDRENgm8o7tX_ddlV-xSry2dwL3VKabs4/explain_spritePivots.gif?psid=1)

这个我们在Cocos2D里面用的不多，starling里面倒是有设置pivot的。

### GUI

-  位图字体（Bitmap Font）

![](https://s2uzig.blu.livefilestore.com/y2p1yTCFixJYHvebhDz3h2KZAuOVgGAL5uJ4U1qW6aKU8OCQzKp8ILBAiiQOipPCYhJTcjqbHz0lKh1nYA0dm520Sdvh1eWAMR3S9mn7iIK-ts/example6.png?psid=1)

这个就是我们今天介绍的重点了。见后面的详细介绍。

-  拆分PSD文件（Split PSD）

![](https://s2uzig.blu.livefilestore.com/y2pKzmCsZz4e2l-KLjpKwa79H3DyEkYNWXy3SsLcESrheKqZ96NVFMJ7Q4C-StIOpKdEAngt0-DeyZXLYCKMUAoP0uB-4q3jJX7hpL5CITouzM/screenshot_splitPSD.jpg?psid=1)

这个功能绝对是美术人员的福利。强烈建议朋友们推荐给自己公司的MM们，给她们介绍下这个ShoeBox，并握着她们的小手手，手把手教她们如何使用这个功能。如果美术是GG，不管了。我不知道其他美术是怎么做的，作为非美术的我，但经常使用Photoshop，我在导出图片的时候，如果图层过多，通过隐藏其他图层，将图层的图片一个个单独导出，其中还要涉及裁剪或者新建文档导出。麻烦不？当然了。所以，这个功能能高效的导出PSD中的图片。

-  Slice 9

![](https://s2uzig.blu.livefilestore.com/y2pJYJnfqiN6YCcgPGWajkRknbVZZUmS-rpkcmnUVm54nnIELzpyg4Y-BGZTzvisRrFny-tMBngIBr77xEn2lVPpmg58hgBX_CPa-hb7MCkv_U/screenshot_slice9.jpg?psid=1)

顾名思义，这个功能处理的是九宫格图片。因为在Cocos2D中有自己的九宫图类，这里就不介绍了。

### Animation

-  动画帧（Ani Frames）

![](https://s2uzig.blu.livefilestore.com/y2pedBCV1Ra7qisRUunEjVBpXbQ-tDSMPEKCOh0FXJZ8YCCFTaP-fcm2GVBwKQvX4Uvx8YKLFNM1pei02xXkJWajvWHNfHytlR1WxEDa6uBxa4/aniFrames.png?psid=1)

![](https://s2uzig.blu.livefilestore.com/y2pcJySjmNkbfkjxDMTc8EtEfrMBUvnmhVvSfXp-vlvIdX7EDF_Pp19YSol-iwQU2KNmZC5SLqZZfymCdvbro4Uq0KX0drfmJjHhAMQQbBIIes/face.gif?psid=1)

将GIF或者SWF动画导出为帧序列图。异常强大！将上面的GIF动画图片拖放到`AniFrames`上后，保存会导出该动画的序列帧。SWF同理。这个功能通常用来解析资源。

-  帧表单（FrameSheet）

![](https://s2uzig.blu.livefilestore.com/y2pCPslpckIepiXngBOQcbybFWXLYA0ViTIma4QXPYnRjllvo5TeSonSLkYapwGDTSgnDwSQf4lxQb1uEVCZamGVQMz3cjKb4Ff51O-7iGlhbM/frameSheet.png?psid=1)

这个功能集合了动画帧序列导出（AniFrames）以及打包精灵图片（Pack Sprites）功能，将GIF或者SWF动画文件拖放到上面后，可以保存为一个精灵表单，内容包含了动画的帧序列图。

### Bitmaps

-  JPNG位图（JPNG Bitmap）

![](https://s2uzig.blu.livefilestore.com/y2pLEc7zkv7VOsG6_IwpA0lLiJiMIK89gnF-Xv8TKOzVkQtH88m8zI1dqKBd7rh8MrTBdO354CeslGtIMXs3H8Vm_kja3SvVmf6GkEcZlVWnPM/JPNG_Bitmap.png?psid=1)

将32位RGBA位图转成24位的JPG格式图，附带一个alpha通道组。引擎解析需要加载JPG图片，然后通过复制右边的alpha通道并应用到左侧的颜色区域来重新组成32位的RGBA图。貌似这种做法现在很少用了。

-  Mask Image

![](https://s2uzig.blu.livefilestore.com/y2pOfE36zQDjbjStwGgzclgAxx-Z0hDle7UWx_aLwD-n1O-YHRiBTfDIV51G49Fk6o1xBYQ66j6iSuR-RYFA5udeBOQYlQ-D2SGLdTSjnomd78/example_maskImage_introduction.jpg?psid=1)

![](https://s2uzig.blu.livefilestore.com/y2pNqsD-h3Dfx51SsopG77sFewwU8SZBU2Nh3Avvf9NPi3LQ132XiFQGHsKVEp4oTFYen7JYvr9hlQRhvA8jMaUA-hW9E8b9xhtSZhG6Wo3UBM/screenshot_maskImage.jpg?psid=1)

将JPG图片进行遮罩和羽化，合成了一张复杂的图。还不清楚这个具体怎么使用。

### Misc

-  Flickr Upload
-  Clean Up Text
-  Mac to PC url

这三个游戏基本用不到。就不介绍了。想要进一步了解的，可以深入官网探个究竟。

## 3 言归正传 创建位图字体

通常情况下，Windows下我们可以使用免费的[Bitmap Font Generator](http://www.angelcode.com/products/bmfont/)，mac下可以使用付费的[Glyph Designer](http://glyphdesigner.71squared.com/)。有了这款跨平台的ShoeBox，美术人员可以完全按照自己的设计，做出一套位图字体，支持中文。

### 工作流程

![](https://s2uzig.blu.livefilestore.com/y2pP64msDHcDjQGa8_iAjVeVjxeUuJXdzFedC6FDP2Ci2iERck2Vc3xPd6o9m3797hW_ncZqa2-yJ32rrLC8QtbGpI1VeNh_NaKnd6rXjFNn-k/bitmap_font_desc.png?psid=1)

1.右键打开设置面板，在`Txt Chars`输入你要制作的字体，如我这里输入`abcdefghijklmnopqrstuvwxyz`这些字体，最后给大家呈现一个`cocos quick`的美术效果。FollowMe！

![](https://dl.dropboxusercontent.com/u/76275795/ShoeBox/example0.png)

2.按住BitmapFont图标2s左右，会复制我们的字符集到剪切板上，这时候打开Photoshop，新建一个文件，用文本输入工具或者热键T点击画布，然后粘帖剪切板上的字符到画布上。如下图所示

![](https://s2uzig.blu.livefilestore.com/y2psTyY4MloXIniJJi60ou53JTRdUuE0PKUfOe6u9CpN6XsT8Nk0TGRqt3oko_T0mqaxynPG7jMUn7hyHIST8QkgjVxJ5Dv53oFve392tw55H4/example1.png?psid=1)

字体毫无美感可言，不是么？

3.下面是发挥设计能力的时候了。让美术尽情设计字体吧。我随便给文字加了个描边、外发光、投影，如下图

![](https://s2uzig.blu.livefilestore.com/y2p-J2TSRJDa4ugrKUjIIBymLgY5Stn0bV-jFhWXRtq5uR82KwJTQ-_qCVUOWk4fw0VOnY7qx2tKRhrZD9kybNm6ysA1Vm6szwrnfAKfhcgr3s/example2.png?psid=1)

4.保存刚才的设计，导出为PNG图片。这里需要注意下，在设计的时候字体的间距不要太靠近，否则ShoeBox在后面识别的过程，会将太过于靠近的字体识别为一个字体。

5.拖放上步保存的图片到BitmapFont图标上，这时候会有流动的虚线表示可以拖放有效。如果你拖个JPG图片的，是无效的。

![](https://s2uzig.blu.livefilestore.com/y2pNEj7did1A9bHEO0ahIkBbhx-NckaNM1i8Tq16Y4UEi8pv_90gLg68_zQ4cdOvHheMV7vQAiXbx3wv4UanyCab-fJ8l5PxktlFlvxWD1YO1A/example3.png?psid=1)

6.见证奇迹的时刻到了！

![](https://s2uzig.blu.livefilestore.com/y2pO-52Va8WM7x3IiUz5vllgi7RF0W1Yh4oGBT1f4pF_RTSMogwxUmYpLopBgWmW6aghE_dtWEHs_8QtwIfVSaxNhX91Dkvw6paWSGYoZ4xBFI/example4.png?psid=1)

在设置面板的 Message 一栏里面，我们输入`cocos quick`，设定字符间距（KerningValue）为合适的值比如-2，空格宽度（TxtSpaceWidth）为5，然后`apply`应用确定，可以看到文字的预览效果。

![](https://s2uzig.blu.livefilestore.com/y2pZnNMMnIXu1W0pjvla79bIcRX7o_kBG7qsmwZAU3bq2ufuRtyPpdht_qMEEjlCEPTETmBOjzcLkcL2uCwgcOlpYygzmlwfQ0Irua7uyAL634/example5.png?psid=1)

7.参数调整合适后，保存字体（SaveFont），会在原始图片目录下生成一个PNG图片和FNT数据文件。这时候引擎就可以直接使用这两个文件了。

如果大家觉得看着文字麻烦，可以先看下视频。

-  [【优酷视频ShoeBox 创建位图字体】](http://v.youku.com/v_show/id_XNjkwMjY2MjI0.html)
-  [【优酷视频ShoeBox 汉字位图例子】](http://v.youku.com/v_show/id_XNjkwMjY1ODg0.html)

Happy Designing!