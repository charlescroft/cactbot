# cactbot (ffxiv raiding overlay)

<img align="right" src="https://raw.githubusercontent.com/OverlayPlugin/cactbot/main/screenshots/cactbot-logo-320x320.png">

[![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/OverlayPlugin/cactbot/test.yml?branch=main)](https://github.com/OverlayPlugin/cactbot/actions?query=workflow%3ATest+branch%3Amain)
[![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/OverlayPlugin/cactbot?color=brightgreen&sort=semver)](https://github.com/OverlayPlugin/cactbot/releases/latest)

🌎 [**English**] [[简体中文](docs/zh-CN/README.md)] [[한국어](docs/ko-KR/README.md)]

1. [About](#about)
1. [Installing](#installing)
1. [Building From Source](#building-from-source)
1. [Overlay Overview](#overlay-overview)
1. [Troubleshooting](#troubleshooting)
1. [Cactbot Customization](#cactbot-customization)
1. [Supported Languages](#supported-languages)

## About

cactbot is an ACT overlay that provides raiding tools for [Final Fantasy XIV](http://www.finalfantasyxiv.com/).
This project depends on
[OverlayPlugin](https://github.com/OverlayPlugin/OverlayPlugin)
and is a plugin for
[Advanced Combat Tracker](http://advancedcombattracker.com/).

cactbot provides these overlays:

* raidboss: built-in timelines and triggers:

![timeline screenshot](screenshots/promo_raidboss_timeline.png)
![triggers screenshot](screenshots/promo_raidboss_triggers.png)

* oopsyraidsy: mistake and death reporting

![oopsy screenshot](screenshots/promo_oopsy.png)

* jobs: condensed gauges with resource, cooldowns, buffs, and procs tracking

![rdm jobs screenshot](screenshots/promo_jobs.png)

* eureka: Adventuring Forays (Eureka/Bozja) tracking map

![eureka screenshot](screenshots/promo_eureka.png)

* radar: hunt mob directions, puller notifications

![radar screenshot](screenshots/promo_radar.png)

* dps: extra features for dps meters

![xephero screenshot](screenshots/xephero.png)

## Installing

### Dependencies

Install [.NET Framework](https://www.microsoft.com/net/download/framework) version 4.6.1 or above.

You must have [DirectX 11](http://imgur.com/TjcnjmG) enabled for Final Fantasy XIV.

Install the 64-bit version of [Advanced Combat Tracker](http://advancedcombattracker.com/), if you have not already.

### Install FFXIV ACT Plugin

If you have just installed ACT,
then you will be presented with a startup wizard.
To get to the startup wizard otherwise,
click on `Options` and then click on `Show Startup Wizard`.

![startup wizard screenshot](screenshots/ffxiv_plugin_show_startup_wizard.png)

In the startup wizard,
select `FFXIV Parsing Plugin` and then click the `Download/Enable Plugin` button.
This will download `%APPDATA%\Advanced Combat Tracker\Plugins\FFXIV_ACT_Plugin.dll`
and enable it in the list of plugins.

![startup wizard download screenshot](screenshots/ffxiv_plugin_parsing_plugin.png)

Alternative FFXIV Plugin Guides:

* [fflogs video guide](https://www.fflogs.com/help/start/)
* [TomRichter guide](https://gist.github.com/TomRichter/e044a3dff5c50024cf514ffb20a201a9#installing-act--ffxiv-plugin)

### Install OverlayPlugin

At this point, if you select the `Plugins` tab and go to the `Plugin Listing`,
your list of plugins should look something like this:

![blank plugin listing screenshot](screenshots/get_plugins_blank.png)

Click on `Get Plugins` to open up the ACT plugin installer.

Select `Overlay Plugin` and then click `Download and Enable`.

![overlay plugin selection screenshot](screenshots/get_plugins_overlayplugin.png)

This will download OverlayPlugin into
`%APPDATA%\Advanced Combat Tracker\Plugins\OverlayPlugin`
and enable `OverlayPlugin.dll` in the list of plugins.

As a note, you must use the [most current fork](https://github.com/OverlayPlugin/OverlayPlugin) of
OverlayPlugin and not the original RainbowMage version or the hibiyasleep fork or the ngld fork.

### Installing cactbot

Again, go to the `Plugins` tab and go to the `Plugin Listing`,
and then select `Get Plugins`.

Select `Cactbot` and then click `Download and Enable`.

![cactbot selection screenshot](screenshots/get_plugins_cactbot.png)

This will download the cactbot into
`%APPDATA%\Advanced Combat Tracker\Plugins\cactbot\cactbot`
and enable `CactbotOverlay.dll` in the list of plugins.

### Plugin Load Order

Because of cactbot’s dependencies,
cactbot must be loaded after both OverlayPlugin and the FFXIV ACT plugin.
Verify that your plugins are in this order:

* FFIXV_ACT_Plugin.dll
* OverlayPlugin.dll
* CactbotOverlay.dll

![plugin order](screenshots/get_plugins_complete.png)

Finally, restart ACT.

## Adding overlays

Here's an example of how to set up the raidboss overlay.
Setting up other cactbot overlays works identically.

1. Open ACT.
1. Make sure you've restarted ACT after adding the cactbot plugin.
1. Navigate to the `Plugins` tab of ACT and then the `OverlayPlugin.dll` tab inside it.
1. Click the "New" button and then pick `Cactbot Raidboss` in the Preset list.

    ![new overlay plugin screenshot](screenshots/overlay_plugin_new.png)

1. At this point, you should see some test UI appear on screen.
cactbot provides default test UI,
a large dashed red border,
and a blue background to help with resizing and placing overlays on screen.
These all go away when the overlay is locked in the config panel for the overlay.
You should always lock your overlay once you are done resizing and placing it.

    ![raidboss plugin unlocked](screenshots/overlay_plugin_new_raidboss_unlocked.png)

1. Type in any name you'd like as the name of this overlay, e.g. `raidbossy`.
1. Click the `OK` button to add this as an Overlay.
It will now appear in the list of overlays in the `Plugins` -> `OverlayPlugin.dll` tab.

1. Drag and resize the overlay to the location that you want it in.

1. In the `General` tab of the `Raidboss` overlay, click the `Lock Overlay` and `Enable Clickthrough` checkboxes.
The test bars, debug text, dashed red border, and shaded blue background will disappear once the overlay has been locked.

    ![raidboss plugin config](screenshots/overlay_plugin_new_raidboss_locked.png)

1. If you want to test the raidboss plugin, see the [Installation Testing guide](docs/FAQ-Troubleshooting.md#installation-testing)]

1. Adding other cactbot overlays is a similar process.
Follow the same process but select a different cactbot preset.

## Building from source

Follow all the steps above for installing cactbot first.
To install dependencies there are 2 methods: **per script** and **manually**

### Dependencies: Script Method

1. `curl` MUST be installed (this is used to download dependencies)
1. Execute `node --loader=ts-node/esm util/fetch_deps.ts` script
1. Continue with **Steps to build**

### Dependencies: Manual Method

1. Please download the latest Zip file from <https://github.com/EQAditu/AdvancedCombatTracker/releases/>
1. Extract the `Advanced Combat Tracker.exe` to `cactbot/plugin/ThirdParty/ACT/`
1. Please download the latest SDK Zip file from <https://github.com/ravahn/FFXIV_ACT_Plugin/> (make sure the file says SDK in its name)
1. Extract the `SDK folder` as well as the `FFXIV_ACT_Plugin.dll` to `cactbot/plugin/ThirdParty/FFXIV_ACT/`
1. Please download the latest Zip file from <https://github.com/OverlayPlugin/OverlayPlugin/releases/>
1. Extract the `libs folder` as well as the `OverlayPlugin.dll` to `cactbot/plugin/ThirdParty/OverlayPlugin/`
1. Continue with **Steps to build**

The folder should look something like this (keep in mind files can change with updates in the future):

```plaintext
ThirdParty
|- ACT
|  |- Advanced Combat Tracker.exe
|- FFXIV_ACT
|  |- SDK
|  |  |- FFXIV_ACT_Plugin.Common.dll
|  |  |- FFXIV_ACT_Plugin.Config.dll
|  |  |- FFXIV_ACT_Plugin.LogFile.dll
|  |  |- FFXIV_ACT_Plugin.Memory.dll
|  |  |- FFXIV_ACT_Plugin.Network.dll
|  |  |- FFXIV_ACT_Plugin.Overlay.dll
|  |  |- FFXIV_ACT_Plugin.Parse.dll
|  |  |- FFXIV_ACT_Plugin.Resource.dll
|  |- FFXIV_ACT_Plugin.dll
|- OverlayPlugin
   |- libs
   |  |- HtmlRenderer.dll
   |  |- Markdig.Signed.dll
   |  |- Newtonsoft.Json.dll
   |  |- OverlayPlugin.Common.dll
   |  |- OverlayPlugin.Core.dll
   |  |- OverlayPlugin.Updater.dll
   |  |- SharpCompress.dll
   |  |- System.ValueTuple.dll
   |  |- websocket-sharp.dll
   |- OverlayPlugin.dll
```

### Steps to build plugin

1. Open the solution in Visual Studio (tested with Visual Studio 2017).
1. Build for "Release" and "x64".
1. The plugin will be built as **bin/x64/Release/CactbotOverlay.dll**.
1. Add the built plugin directly as an ACT plugin.  In the ACT -> Plugins -> Plugin Listing tab, click the `Browse` button and find the **bin/x64/Release/CactbotOverlay.dll** where this file was built.  Then click `Add/Enable Plugin`.

### npm and webpack

If you are not a cactbot developer
and are trying to modify cactbot for your own personal triggers,
you should instead refer to the [customization documentation](docs/CactbotCustomization.md)
instead of changing your local cactbot files.

To install npm and start Webpack, follow these steps:

1. Install [nodejs LTS and npm](https://nodejs.org/en/download/)
1. Run `npm install` in the root of the cactbot directory.
1. Run `npm run build` or `npm start`.

Cactbot should always work with the latest LTS release.
If this is not the case, file an issue.

See the [contributing](CONTRIBUTING.md#validating-changes-via-webpack) documentation
for more details about using Webpack.

## Overlay Overview

The [ui/](ui/) directory contains cactbot's overlays.
If you installed cactbot following the instructions above,
this will most likely be `%APPDATA%\Advanced Combat Tracker\Plugins\cactbot\cactbot\ui\`.

Each cactbot overlay should be added as a separate overlay.
See the [Adding Overlays](#adding-overlays) section for more details about setup.

### [raidboss](ui/raidboss) overlay

To use this overlay,
point cactbot at **ui/raidboss/raidboss.html** or use the `Cactbot Raidboss` preset.

This overlay provides a visual timeline of upcoming events in a fight, as well as text and audio
notifications to help increase raid awareness. Text and sound alerts can be based on the fight
timeline, or come from log messages that occur in the game, similar to ACT's "Custom Triggers".
The overlay is designed to look and feel similar to the
[BigWigs Bossmods](https://www.curseforge.com/wow/addons/big-wigs) addon for World of Warcraft.

[This page](https://overlayplugin.github.io/cactbot/util/coverage/coverage.html) lists
the currently supported set of content in cactbot.
Support is continually added over time (patches welcome!)
but a lot of old content may not be supported yet.

Fight timelines are provided in files designed for the [ACT Timeline](https://github.com/grindingcoil/act_timeline)
plugin, [documented here](https://web.archive.org/web/20230426121530/https://dtguilds.enjin.com/forum/m/37032836/viewthread/26353492-act-timeline-plugin)
with [some extensions](docs/TimelineGuide.md).

There are three levels of text alerts, in order of escalating importance: `info`, `alert`, and `alarm`.
Text messages will be in one of these, and more important levels are larger and more eye grabbing colors.  Text-to-speech can be configured if you prefer that over on screen text.

Timeline files and triggers for text and sound alerts are found in [ui/raidboss/data](ui/raidboss/data), timeline files with `.txt` extension and trigger files with `.ts` extension.

In this screenshot, the raidboss overlay is highlighted, with the timeline circled in red, and the
text alerts circled in yellow, with an `alert`-level text message visible.

![raidboss screenshot](screenshots/Raidboss.png)

### raidboss emulator

If you are writing triggers or timelines and want to test them, you can use the raidboss emulator:
**ui/raidboss/raidemulator.html**.

This currently can only be loaded in a browser and not as an overlay.
This will work in current version of Chrome,
and should work in other browsers as well but this is less tested.

If you want the emulator to use your ACT settings and user triggers,
you will need to enable the OverlayPlugin WS Server via the following instructions:

1. Start ACT.
1. Start the WS Server via Plugins -> OverlayPlugin WSServer -> Stream/Local Overlay.

If you're developing triggers for the cactbot repository,
you can start a local development server via `npm run start`
and load the overlay in Chrome via `http://127.0.0.1:8080/ui/raidboss/raidemulator.html?OVERLAY_WS=ws://127.0.0.1:10501/ws`

If you're developing user triggers,
you can load the overlay in Chrome via `https://overlayplugin.github.io/cactbot/ui/raidboss/raidemulator.html?OVERLAY_WS=ws://127.0.0.1:10501/ws`

If you're trying to reproduce an issue,
you can load the overlay in Chrome via `https://overlayplugin.github.io/cactbot/ui/raidboss/raidemulator.html`.
You don't need the WS Server running in this case.

Once you've got the overlay loaded, you can follow these instructions to use the emulator.

1. Drag and drop a [network log](/docs/FAQ-Troubleshooting.md#how-to-find-a-network-log) onto the page.
1. Select the zone and encounter, and then click `Load Encounter`.

If the emulator is not working, check the console log in the inspector for errors.

![raidboss emulator screenshot](screenshots/raidboss_emulator.png)

### [oopsyraidsy](ui/oopsyraidsy) overlay

To use this overlay,
point cactbot at **ui/oopsyraidsy/oopsyraidsy.html** or use the `Cactbot OopsyRaidsy` preset.

This overlay provides mistake tracking and death reporting.
Oopsy raidsy is meant to reduce the time wasted understanding what went wrong on fights and how people died.
During the fight, only a limited number of mistakes are shown (to avoid clutter),
but afterwards a full scrollable list is displayed.

When somebody dies, the last thing they took damage from is listed in the log.  For example, if the log specifies: ":skull: Poutine: Iron Chariot (82173/23703)" this means that Poutine most likely died to Iron Chariot, taking 82173 damage and having 23703 health at the time.  The health value itself is not perfect and may be slightly out of date by a ~second due to a hot tick or multiple simultaneous damage sources.

When mistakes are made that are avoidable, oopsy logs warning (:warning:) and failure (:no_entry_sign:) messages, explaining what went wrong.

Mistake triggers are specified for individual fights in the [ui/oopsyraidsy/data](ui/oopsyraidsy/data) folder.

![oopsy screenshot](screenshots/promo_oopsy.png)

You can copy oopsy lines to the clipboard by clicking them.
(You may need to uncheck `Enable Clickthrough` checkbox from the OverlayPlugin option.)

### [jobs](ui/jobs) overlay

To use this overlay,
point cactbot at **ui/jobs/jobs.html** or use the `Cactbot Jobs` preset.

This overlay includes 3 parts: a resource zone at top middle, a raidbuff zone at top right, and a tracking zone at bottom.

* **resource zone**: HP bars and job-specific resource, along with some special counters for some jobs.
* **tracking zone**: job-specific important buff/debuff duration, cooldowns and procs.
* **raidbuff zone**: important raidbuffs duration and coming cooldowns.

You can change some of the behavior or appearance via the user panel, e.g. only show the raidbuff zone, or enable compact view.

However, customization of some behavior like cooldown alert thresholds and element order is not available for now.

In this screenshot, the RDM jobs UI is shown as an example.
The in-game UI is shown at top and the jobs overlay is shown at bottom.
HP & MP bar, White Mana and Black Mana are shown in purple.
The right yellow is raidbuff icon.
Verstone Ready duration, Verfire Ready duration, Fleche cooldown, Contre Sixte cooldown are shown in red.

![jobs screenshot](screenshots/Jobs.png)

#### Features for Each Job

<details>
<summary>Job Features Table (Click to expand)</summary>

|Job|Feature (left to right, top to bottom)|
|:-:|-|
|<img src="./resources/ffxiv/jobs/pld-large.png" width="30px"/><br> Paladin|**Resource zone**: Oath Gauge,  combo timer, Requiescat stacks (if under Requiescat). <br> **Tracking zone**: Expiacion cooldown, Circle of Scorn cooldown, Requiescat cooldown, Fight or Flight duration & cooldown.|
|<img src="./resources/ffxiv/jobs/war-large.png" width="30px"/><br> Warrior|**Resource zone**: Beast Gauge, combo timer. <br> **Tracking zone**: Surging Tempest buff duration, Upheaval/Orogeny cooldown, Inner Release cooldown.|
|<img src="./resources/ffxiv/jobs/drk-large.png" width="30px"/><br> Dark Knight|**Resource zone**: Blood Gauge, combo timer. <br> **Tracking zone**: Darkside duration, Delirium cooldown, Salted Earth cooldown, Living Shadow cooldown.|
|<img src="./resources/ffxiv/jobs/gnb-large.png" width="30px"/><br> Gunbreaker|**Resource zone**: Cartridge amount, combo timer. <br> **Tracking zone**: Gnashing Fang cooldown, No Mercy duration & cooldown, Bloodfest cooldown.|
|<img src="./resources/ffxiv/jobs/whm-large.png" width="30px"/><br> White Mage|**Resource zone**: Lily timer, Lily & Blood Lily amount. <br> **Tracking zone**: Dia/Aero DoT duration, Assize cooldown, Presence Of Mind cooldown, Lucid Dreaming cooldown.|
|<img src="./resources/ffxiv/jobs/sch-large.png" width="30px"/><br> Scholar|**Resource zone**: Aetherflow stack, Faerie Gauge/Seraph duration. <br> **Tracking zone**: Bio DoT duration, Aetherflow cooldown, Lucid Dreaming cooldown.|
|<img src="./resources/ffxiv/jobs/ast-large.png" width="30px"/><br> Astrologian|**Resource zone**: Held Card. <br> **Tracking zone**: Combust DoT duration, Draw cooldown, Lucid Dreaming cooldown.|
|<img src="./resources/ffxiv/jobs/sge-large.png" width="30px"/><br> Sage|**Resource zone**: Addersgall timer, Addersgall & Addersting amount. <br> **Tracking zone**: Eukrasian Dosis DoT duration, Phlegma cooldown, Psyche cooldown, Lucid Dreaming cooldown.|
|<img src="./resources/ffxiv/jobs/mnk-large.png" width="30px"/><br> Monk|**Resource zone**: chakra stack, form timer, Master's Gauge (Fury stack, Nadi & Beast Chakra). <br> **Tracking zone**: Perfect Balance cooldown, Riddle Of Fire cooldown, Riddle of Wind cooldown, Brotherhood cooldown.|
|<img src="./resources/ffxiv/jobs/drg-large.png" width="30px"/><br> Dragoon|**Resource zone**: Firstminds' Focus stack, combo timer. <br> **Tracking zone**: Power Surge buff duration, Jump cooldown, Lance Charge duration & cooldown, Geirskogul duration & cooldown.|
|<img src="./resources/ffxiv/jobs/nin-large.png" width="30px"/><br> Ninja|**Resource zone**: Ninki amount, Kazematoi stack, combo timer. <br> **Tracking zone**: Trick Attack duration & cooldown, Bunshin cooldown, Mudra cooldown.|
|<img src="./resources/ffxiv/jobs/sam-large.png" width="30px"/><br> Samurai|**Resource zone**: Kenki amount, Meditation stack, combo timer, held Sen. <br> **Tracking zone**: Fugetsu buff duration, Fuka buff duration, Higanbana DoT duration, Ikishoten cooldown.|
|<img src="./resources/ffxiv/jobs/rpr-large.png" width="30px"/><br> Reaper|**Resource zone**: Soul amount, Shroud amount, combo timer, Lemure Shroud/Void Shroud stack (under Enshroud). <br> **Tracking zone**: Death's Design duration, Soul Slice/Soul Scythe cooldown, Gluttony cooldown, Arcane Circle duration & cooldown.|
|<img src="./resources/ffxiv/jobs/vpr-large.png" width="30px"/><br> Viper|**Resource zone**: Rattling Coil Stack, Serpent Offerings amount, combo timer, ViperSight gauge. <br> **Tracking zone**: Hunter's Instinct duration, Swiftscaled duration, Vice Combo cooldown.|
|<img src="./resources/ffxiv/jobs/brd-large.png" width="30px"/><br> Bard|**Resource zone**: Repertoire stack, Soul Voice amount, Repertoire tick timer, held Coda. <br> **Tracking zone**: Windbite/Venomous Bite DoT duration, Song duration, Empyreal Arrow cooldown, Hawk's Eyes/Barrage proc duration.|
|<img src="./resources/ffxiv/jobs/mch-large.png" width="30px"/><br> Machinist|**Resource zone**: Heat/Overheated stack, Battery/Automaton Queen duration, combo timer, Wildfire GCD counter (if Wildfire active). <br> **Tracking zone**: Drill/Bioblaster cooldown, Air Anchor cooldown, Chain Saw cooldown, Wildfire duration & cooldown.|
|<img src="./resources/ffxiv/jobs/dnc-large.png" width="30px"/><br> Dancer|**Resource zone**: Fourfold Feather amount, Esprit amount, combo timer. <br> **Tracking zone**: Standard Step cooldown, Technical Step duration & cooldown, Flourish duration & cooldown.|
|<img src="./resources/ffxiv/jobs/blm-large.png" width="30px"/><br> Black Mage|**Resource zone**: Umbral Ice/Astral Fire duration, Polyglot timer, MP tick timer, Umbral Hearts stack & Polyglot stack, Astral Soul stack. <br> **Tracking zone**: Firestarter proc duration, Thunderhead proc duration, Thunder DoT duration, Manafont cooldown.|
|<img src="./resources/ffxiv/jobs/smn-large.png" width="30px"/><br> Summoner|**Resource zone**: Trance/Attunement duration, Aetherflow stack, Arcanum held and Attunement stored in. <br> **Tracking zone**: Energy Drain/Energy Siphon cooldown, Summon Bahamut/Phoenix cooldown, Lucid Dreaming cooldown.|
|<img src="./resources/ffxiv/jobs/rdm-large.png" width="30px"/><br> Red Mage|**Resource zone**: White Mana and Black Mana amount, Mana Stack (if any). <br> **Tracking zone**: Verstone Ready duration, Verfire Ready duration, Fleche cooldown, Contre Sixte cooldown.|
|<img src="./resources/ffxiv/jobs/pct-large.png" width="30px"/><br> Pictomancer|**Resource zone**: Palette gauge, White/Black Paint Stack, Hammer Time timer, Living Canvas, Portrait. <br> **Tracking zone**: Living Muse cooldown, Steel Muse cooldown, Scenic Muse cooldown.|
|<img src="./resources/ffxiv/jobs/blu-large.png" width="30px"/><br> Blue Mage|**Resource zone**: none. <br> **Tracking zone**: Off-guard/Peculiar Light cooldown, Song of Torment/Nightbloom/Aetherial Spark DoT duration, Lucid Dreaming cooldown.|

</details>

### [eureka](ui/eureka) overlay

To use this overlay,
point cactbot at **ui/eureka/eureka.html** or use the `Cactbot Eureka` preset.

This overlay provides automatic tracking of NMs that are popped or have
been killed.  It shows gales/night timers and any local tracker link
that has been pasted in chat.  Any flags in chat are also temporarily
included on the map.

It currently does not read the tracker information directly.  However,
if you click on the left/red "Copy killed NMs" button in the tracker to
copy the list of currently dead NMs, you can paste it in game, e.g.
`/echo NMs on cooldown: Serket (7m) → Julika (24m) → Poly (54m)`

If you do not see the emoji, make sure you have installed [this Windows update](https://support.microsoft.com/en-us/help/2729094/an-update-for-the-segoe-ui-symbol-font-in-windows-7-and-in-windows-ser).

![eureka screenshot](screenshots/promo_eureka.png)

### [radar](ui/radar) overlay

To use this overlay,
point cactbot at **ui/radar/radar.html** or use the `Cactbot Radar` preset.

This overlay lets you know about nearby hunt mobs (S-rank, A-rank, etc).
When one pops, it gives you an arrow (based on your character's heading)
and a distance to the mob.

There are options to show who pulled the mob,
as well as to configure the display of the radar.
You can also set up custom options for different ranks
(e.g. make noises for S rank, but be silent for B ranks),
or set up custom triggers for any mob name you would like.

See the `cactbot/user/radar-example.js` for more options.

![radar screenshot](screenshots/promo_radar.png)

### [dps](ui/dps) meter overlays

cactbot has a couple of dps meters as well with a few extra features.
They hide when you change zones, so it's not something you need to manually hide or show.

The [xephero](ui/dps/xephero) dps meter is based on the same dps meter built for miniparse.

![xephero screenshot](screenshots/xephero.png)

The [rdmty](ui/dps/rdmty) dps meter is based on the same dps meter for miniparse, and
recolored to match [fflogs](http://fflogs.com).

![rdmty screenshot](screenshots/rdmty.png)

### [pull counter](ui/pullcounter) overlay

This small overlay sticks the current pull count for raiding bosses on screen.
This is primarily for folks who stream a lot and want to review video footage.
Having a number on screen makes it easy to scrub through video and find
particular pulls to review.

In most cases, you can reset the count for the current boss/zone by typing
`/echo pullcounter reset`.
You can also edit the counts directly in your
`%APPDATA%\Advanced Combat Tracker\Config\RainbowMage.OverlayPlugin.config.json`
file.

![pull counter screenshot](screenshots/pullcounter.png)

### [test](ui/test) overlay

To use this overlay,
point cactbot at **ui/test/test.html** or use the `Cactbot Test` preset.

This overlay is just an onscreen test of cactbot variables and is not meant to be used while playing.
It can be useful to try out to make sure everything is working as expected or to use to help debug overlay issues.

![test screenshot](screenshots/test.png)

## Troubleshooting

A general FAQ can be found [here](docs/FAQ-Troubleshooting.md) containing solutions to common Cactbot issues.

## Cactbot Customization

Most common cactbot configuration can be done via the control panel,
inside of ACT.

![config panel](screenshots/config_panel.png)

This can be found by going to
Plugins -> OverlayPlugin.dll -> Cactbot Event Source,
and then clicking on options there.

In particular,
if you want to use text to speech for raidboss alerts,
you can change the "Default alert output" to be
"TTS Only" or "Text and TTS".
You can also change this on a per trigger basis.

Or, if for some reason (???) you don't want the ready check sound alert,
you can disable this via the same options panel.
Go to Raidboss -> General Triggers -> General -> General Ready Check,
and set it to `Disabled` instead of `Defaults`.

These options are stored in your
`%APPDATA%\Advanced Combat Tracker\Config\RainbowMage.OverlayPlugin.config.json`
file.
It is not recommended to edit this file directly,
as it must be [strict json](https://jsonlint.com/)
and ACT might fail to load if the file is written incorrectly.

It is recommended that you do most of your configuration via this control panel
rather than with user files.
Files in `cactbot/user/` are more powerful
and can override anything from the control panel.
However, this can also be confusing when the control panel doesn't adjust something
properly that a `cactbot/user/` file is overriding silently.

See [this documentation](docs/CactbotCustomization.md#user-folder-config-overrides)
for more details about user javascript and css files.

## Supported Languages

cactbot is tested and works with the current
international (English, German, French, Japanese) version,
the current Chinese version,
and the current Korean version.
Some translations are still a work in progress.
See the [cactbot coverage page](https://overlayplugin.github.io/cactbot/util/coverage/coverage.html) for more details.

## Licensing, Trademarks, Copyright

cactbot is open source under the [Apache License, Version 2.0](LICENSE).

FINAL FANTASY is a registered trademark of Square Enix Holdings Co., Ltd.

Final Fantasy art and icons reused non-commercially under the
[FINAL FANTASY® XIV Materials Usage License](https://support.na.square-enix.com/rule.php?id=5382).

See the [LICENSE](LICENSE) file for more details about other bundled projects.

```
cactbot
├─ 📁.github
│  ├─ 📁ISSUE_TEMPLATE
│  │  ├─ 📄01-bug_report.yml
│  │  ├─ 📄02-question.yml
│  │  ├─ 📄03-feature_request.yml
│  │  └─ 📄config.yml
│  ├─ 📁actions
│  │  └─ 📁setup-js-env
│  │     └─ 📄action.yml
│  ├─ 📁matchers
│  │  ├─ 📄markdownlint.json
│  │  ├─ 📄msbuild.json
│  │  ├─ 📄pylint.json
│  │  └─ 📄stylelint.json
│  ├─ 📁scripts
│  │  ├─ 📄auto-label.cjs
│  │  ├─ 📄lint-pr-title.cjs
│  │  ├─ 📄lint-workflow.cjs
│  │  ├─ 📄npm-package.cjs
│  │  └─ 📄pr-review.cjs
│  ├─ 📁workflows
│  │  ├─ 📄README.md
│  │  ├─ 📄build-artifact.yml
│  │  ├─ 📄css-lint.yml
│  │  ├─ 📄javascript-lint.yml
│  │  ├─ 📄label-pr-review.yml
│  │  ├─ 📄label-pr.yml
│  │  ├─ 📄lint-pr-title.yaml
│  │  ├─ 📄markdown-lint.yml
│  │  ├─ 📄post-process.yml
│  │  ├─ 📄release.yml
│  │  ├─ 📄test-sync-files.yml
│  │  ├─ 📄test-validate-versions.yml
│  │  ├─ 📄test.yml
│  │  ├─ 📄update-gh-pages.yml
│  │  ├─ 📄update-triggers-branch.yml
│  │  ├─ 📄update_logdefs.yml
│  │  └─ 📄workflow-lint.yml
│  ├─ 📄dependabot.yml
│  └─ 📄logdef_update_pr_template.md
├─ 📁.vscode
│  ├─ 📄extensions.json
│  └─ 📄settings.json
├─ 📁docs
│  ├─ 📁images
│  │  ├─ 📄cheatengine_addresslist.png
│  │  ├─ 📄cheatengine_browsememory.png
│  │  ├─ 📄cheatengine_connected.png
│  │  ├─ 📄cheatengine_debugger.png
│  │  ├─ 📄cheatengine_debugger2.png
│  │  ├─ 📄cheatengine_disassembly.png
│  │  ├─ 📄cheatengine_disassembly2.png
│  │  ├─ 📄cheatengine_found.png
│  │  ├─ 📄cheatengine_initial.png
│  │  ├─ 📄cheatengine_initialscan.png
│  │  ├─ 📄cheatengine_pointer.png
│  │  ├─ 📄cheatengine_postscan.png
│  │  ├─ 📄cheatengine_signature_scan.png
│  │  ├─ 📄cheatengine_tracing.png
│  │  ├─ 📄cheatengine_tracing2.png
│  │  ├─ 📄logguide_dumpnetworkdata.png
│  │  ├─ 📄logguide_ffxivmon.png
│  │  ├─ 📄logguide_ffxivmon_import.png
│  │  ├─ 📄logguide_import.png
│  │  ├─ 📄logguide_includehp.png
│  │  ├─ 📄logguide_networkdata.png
│  │  ├─ 📄logguide_viewlogs.png
│  │  ├─ 📄newpatch_testoverlay.png
│  │  ├─ 📄remote_devtools.png
│  │  ├─ 📄remote_itworks.png
│  │  ├─ 📄remote_playerselect.png
│  │  ├─ 📄remote_testui.png
│  │  ├─ 📄remote_wsserver.png
│  │  ├─ 📄timelineguide_copy.png
│  │  ├─ 📄timelineguide_encounterlogs.png
│  │  ├─ 📄timelineguide_timeline.png
│  │  ├─ 📄troubleshooting_chatlogfilter.png
│  │  ├─ 📄troubleshooting_hidechatlog.png
│  │  ├─ 📄troubleshooting_networklog.png
│  │  ├─ 📄troubleshooting_openlogfolder.png
│  │  ├─ 📄vfxeditor_initial.png
│  │  ├─ 📄vfxeditor_loaded.png
│  │  ├─ 📄vfxeditor_replace.png
│  │  └─ 📄vfxeditor_result.png
│  ├─ 📁ko-KR
│  │  ├─ 📄CactbotCustomization.md
│  │  └─ 📄README.md
│  ├─ 📁logs
│  │  └─ 📄TheAbyssalFractureExtreme.log
│  ├─ 📁zh-CN
│  │  ├─ 📄CactbotCustomization.md
│  │  ├─ 📄MemorySignatures.md
│  │  ├─ 📄README.md
│  │  ├─ 📄RaidbossGuide.md
│  │  └─ 📄TimelineGuide.md
│  ├─ 📁zh-TW
│  │  └─ 📄CactbotCustomization.md
│  ├─ 📄CactbotCustomization.md
│  ├─ 📄FAQ-Troubleshooting.md
│  ├─ 📄Headmarkers.md
│  ├─ 📄LogGuide.md
│  ├─ 📄MemorySignatures.md
│  ├─ 📄OopsyraidsyGuide.md
│  ├─ 📄PatchUpdateChecklist.md
│  ├─ 📄RaidbossGuide.md
│  ├─ 📄RemoteCactbot.md
│  └─ 📄TimelineGuide.md
├─ 📁eslint
│  ├─ 📄cactbot-locale-order.js
│  ├─ 📄cactbot-output-strings.js
│  ├─ 📄cactbot-response-default-severities.js
│  ├─ 📄cactbot-timeline-triggers.js
│  ├─ 📄cactbot-trigger-property-order.js
│  ├─ 📄cactbot-triggerset-property-order.js
│  ├─ 📄eslint-utils.js
│  └─ 📄package.json
├─ 📁plugin
│  ├─ 📁CactbotEventSource
│  │  ├─ 📁Properties
│  │  │  └─ 📄AssemblyInfo.cs
│  │  ├─ 📁loc
│  │  │  ├─ 📄Strings.Designer.cs
│  │  │  ├─ 📄Strings.resx
│  │  │  └─ 📄Strings.zh.resx
│  │  ├─ 📄CactbotEventSource.cs
│  │  ├─ 📄CactbotEventSource.csproj
│  │  ├─ 📄CactbotEventSourceConfig.cs
│  │  ├─ 📄CactbotPathWarning.cs
│  │  ├─ 📄FFXIVPlugin.cs
│  │  ├─ 📄FFXIVProcess.cs
│  │  ├─ 📄FFXIVProcessCn.cs
│  │  ├─ 📄FFXIVProcessIntl.cs
│  │  ├─ 📄FFXIVProcessKo.cs
│  │  ├─ 📄JSEvents.cs
│  │  ├─ 📄NativeMethods.cs
│  │  └─ 📄VersionChecker.cs
│  ├─ 📁CactbotOverlay
│  │  ├─ 📁Properties
│  │  │  └─ 📄AssemblyInfo.cs
│  │  ├─ 📁loc
│  │  │  ├─ 📄Strings.Designer.cs
│  │  │  ├─ 📄Strings.resx
│  │  │  └─ 📄Strings.zh.resx
│  │  ├─ 📄AssemblyResolver.cs
│  │  ├─ 📄CactbotOverlay.csproj
│  │  └─ 📄PluginLoader.cs
│  ├─ 📁ThirdParty
│  └─ 📄Cactbot.sln
├─ 📁resources
│  ├─ 📁ffxiv
│  │  ├─ 📁jobs
│  │  │  ├─ 📄acn.png
│  │  │  ├─ 📄alc.png
│  │  │  ├─ 📄arc.png
│  │  │  ├─ 📄arm.png
│  │  │  ├─ 📄ast-framed.png
│  │  │  ├─ 📄ast-large.png
│  │  │  ├─ 📄ast.png
│  │  │  ├─ 📄blm-framed.png
│  │  │  ├─ 📄blm-large.png
│  │  │  ├─ 📄blm.png
│  │  │  ├─ 📄blu-framed.png
│  │  │  ├─ 📄blu-large.png
│  │  │  ├─ 📄blu.png
│  │  │  ├─ 📄bot.png
│  │  │  ├─ 📄brd-framed.png
│  │  │  ├─ 📄brd-large.png
│  │  │  ├─ 📄brd.png
│  │  │  ├─ 📄bsm.png
│  │  │  ├─ 📄cho.png
│  │  │  ├─ 📄cnj.png
│  │  │  ├─ 📄crp.png
│  │  │  ├─ 📄cul.png
│  │  │  ├─ 📄dnc-framed.png
│  │  │  ├─ 📄dnc-large.png
│  │  │  ├─ 📄dnc.png
│  │  │  ├─ 📄drg-framed.png
│  │  │  ├─ 📄drg-large.png
│  │  │  ├─ 📄drg.png
│  │  │  ├─ 📄drk-framed.png
│  │  │  ├─ 📄drk-large.png
│  │  │  ├─ 📄drk.png
│  │  │  ├─ 📄fsh.png
│  │  │  ├─ 📄gla.png
│  │  │  ├─ 📄gnb-framed.png
│  │  │  ├─ 📄gnb-large.png
│  │  │  ├─ 📄gnb.png
│  │  │  ├─ 📄gsm.png
│  │  │  ├─ 📄limit break.png
│  │  │  ├─ 📄lnc.png
│  │  │  ├─ 📄ltw.png
│  │  │  ├─ 📄mch-framed.png
│  │  │  ├─ 📄mch-large.png
│  │  │  ├─ 📄mch.png
│  │  │  ├─ 📄min.png
│  │  │  ├─ 📄mnk-framed.png
│  │  │  ├─ 📄mnk-large.png
│  │  │  ├─ 📄mnk.png
│  │  │  ├─ 📄mrd.png
│  │  │  ├─ 📄nin-framed.png
│  │  │  ├─ 📄nin-large.png
│  │  │  ├─ 📄nin.png
│  │  │  ├─ 📄pct-framed.png
│  │  │  ├─ 📄pct-large.png
│  │  │  ├─ 📄pct.png
│  │  │  ├─ 📄pet.png
│  │  │  ├─ 📄pgl.png
│  │  │  ├─ 📄pld-framed.png
│  │  │  ├─ 📄pld-large.png
│  │  │  ├─ 📄pld.png
│  │  │  ├─ 📄rdm-framed.png
│  │  │  ├─ 📄rdm-large.png
│  │  │  ├─ 📄rdm.png
│  │  │  ├─ 📄rog.png
│  │  │  ├─ 📄rpr-framed.png
│  │  │  ├─ 📄rpr-large.png
│  │  │  ├─ 📄rpr.png
│  │  │  ├─ 📄sam-framed.png
│  │  │  ├─ 📄sam-large.png
│  │  │  ├─ 📄sam.png
│  │  │  ├─ 📄sch-framed.png
│  │  │  ├─ 📄sch-large.png
│  │  │  ├─ 📄sch.png
│  │  │  ├─ 📄sge-framed.png
│  │  │  ├─ 📄sge-large.png
│  │  │  ├─ 📄sge.png
│  │  │  ├─ 📄smn-framed.png
│  │  │  ├─ 📄smn-large.png
│  │  │  ├─ 📄smn.png
│  │  │  ├─ 📄thm.png
│  │  │  ├─ 📄vpr-framed.png
│  │  │  ├─ 📄vpr-large.png
│  │  │  ├─ 📄vpr.png
│  │  │  ├─ 📄war-framed.png
│  │  │  ├─ 📄war-large.png
│  │  │  ├─ 📄war.png
│  │  │  ├─ 📄whm-framed.png
│  │  │  ├─ 📄whm-large.png
│  │  │  ├─ 📄whm.png
│  │  │  └─ 📄wvr.png
│  │  ├─ 📁status
│  │  │  ├─ 📄arcane-circle.png
│  │  │  ├─ 📄arrow.png
│  │  │  ├─ 📄astral.png
│  │  │  ├─ 📄balance.png
│  │  │  ├─ 📄battle-litany.png
│  │  │  ├─ 📄battlevoice.png
│  │  │  ├─ 📄bole.png
│  │  │  ├─ 📄brotherhood.png
│  │  │  ├─ 📄chain-stratagem.png
│  │  │  ├─ 📄contagion.png
│  │  │  ├─ 📄devilment.png
│  │  │  ├─ 📄devotion.png
│  │  │  ├─ 📄divination.png
│  │  │  ├─ 📄dokumori.png
│  │  │  ├─ 📄dragon-sight.png
│  │  │  ├─ 📄earth.png
│  │  │  ├─ 📄embolden.png
│  │  │  ├─ 📄ewer.png
│  │  │  ├─ 📄fire.png
│  │  │  ├─ 📄foes-requiem.png
│  │  │  ├─ 📄food.png
│  │  │  ├─ 📄hypercharge.png
│  │  │  ├─ 📄ice.png
│  │  │  ├─ 📄lady-of-crowns.png
│  │  │  ├─ 📄lightning.png
│  │  │  ├─ 📄lord-of-crowns.png
│  │  │  ├─ 📄mug.png
│  │  │  ├─ 📄offguard.png
│  │  │  ├─ 📄peculiar-light.png
│  │  │  ├─ 📄physical.png
│  │  │  ├─ 📄potion.png
│  │  │  ├─ 📄radiant-finale.png
│  │  │  ├─ 📄searing-light-6.0.png
│  │  │  ├─ 📄searing-light.png
│  │  │  ├─ 📄spear.png
│  │  │  ├─ 📄spire.png
│  │  │  ├─ 📄standard-finish.png
│  │  │  ├─ 📄starry-muse.png
│  │  │  ├─ 📄technical-finish.png
│  │  │  ├─ 📄trick-attack.png
│  │  │  ├─ 📄umbral.png
│  │  │  ├─ 📄water.png
│  │  │  └─ 📄wind.png
│  │  └─ 📄LICENSE.txt
│  ├─ 📁images
│  │  └─ 📄06ew_raid_p12s_classic2_noflip.webp
│  ├─ 📁sounds
│  │  ├─ 📁BigWigs
│  │  │  ├─ 📄Alarm.webm
│  │  │  ├─ 📄Alert.webm
│  │  │  ├─ 📄Info.webm
│  │  │  ├─ 📄Long.webm
│  │  │  └─ 📄license.txt
│  │  ├─ 📁Overwatch
│  │  │  ├─ 📄D.Va_-_Boosters_engaged.webm
│  │  │  ├─ 📄D.Va_-_Game_on.webm
│  │  │  ├─ 📄Hanzo_-_Sake.webm
│  │  │  ├─ 📄LICENSE.txt
│  │  │  └─ 📄Reaper_-_Die_die_die.webm
│  │  └─ 📁freesound
│  │     ├─ 📁amy
│  │     │  ├─ 📄1.webm
│  │     │  ├─ 📄10.webm
│  │     │  ├─ 📄2.webm
│  │     │  ├─ 📄3.webm
│  │     │  ├─ 📄4.webm
│  │     │  ├─ 📄5.webm
│  │     │  ├─ 📄6.webm
│  │     │  ├─ 📄7.webm
│  │     │  ├─ 📄8.webm
│  │     │  └─ 📄9.webm
│  │     ├─ 📄LICENSE.txt
│  │     ├─ 📄alarm.webm
│  │     ├─ 📄percussion_hit.webm
│  │     ├─ 📄power_up.webm
│  │     ├─ 📄sonar.webm
│  │     └─ 📄water_drop.webm
│  ├─ 📄conditions.ts
│  ├─ 📄content_list.ts
│  ├─ 📄content_type.ts
│  ├─ 📄datetime.ts
│  ├─ 📄defaults.css
│  ├─ 📄effect_id.ts
│  ├─ 📄hunt.ts
│  ├─ 📄languages.ts
│  ├─ 📄netlog_defs.ts
│  ├─ 📄netregexes.ts
│  ├─ 📄not_reached.ts
│  ├─ 📄outputs.ts
│  ├─ 📄overlay_plugin_api.ts
│  ├─ 📄party.ts
│  ├─ 📄pet_names.ts
│  ├─ 📄player_override.ts
│  ├─ 📄regexes.ts
│  ├─ 📄resourcebar.ts
│  ├─ 📄responses.ts
│  ├─ 📄stringhandlers.ts
│  ├─ 📄timerbar.ts
│  ├─ 📄timerbox.ts
│  ├─ 📄timericon.ts
│  ├─ 📄translations.ts
│  ├─ 📄user_config.ts
│  ├─ 📄util.ts
│  ├─ 📄weather.ts
│  ├─ 📄weather_rate.ts
│  ├─ 📄widget_list.ts
│  ├─ 📄world_id.ts
│  ├─ 📄zone_id.ts
│  └─ 📄zone_info.ts
├─ 📁screenshots
│  ├─ 📄Jobs.png
│  ├─ 📄Raidboss.png
│  ├─ 📄cactbot-logo-320x320.png
│  ├─ 📄config_panel.png
│  ├─ 📄ffxiv_plugin_parsing_plugin.png
│  ├─ 📄ffxiv_plugin_show_startup_wizard.png
│  ├─ 📄fishing.png
│  ├─ 📄get_plugins_blank.png
│  ├─ 📄get_plugins_cactbot.png
│  ├─ 📄get_plugins_complete.png
│  ├─ 📄get_plugins_overlayplugin.png
│  ├─ 📄overlay_plugin_new.png
│  ├─ 📄overlay_plugin_new_raidboss_locked.png
│  ├─ 📄overlay_plugin_new_raidboss_unlocked.png
│  ├─ 📄promo_eureka.png
│  ├─ 📄promo_fishing.png
│  ├─ 📄promo_jobs.png
│  ├─ 📄promo_oopsy.png
│  ├─ 📄promo_radar.png
│  ├─ 📄promo_raidboss_timeline.png
│  ├─ 📄promo_raidboss_triggers.png
│  ├─ 📄pullcounter.png
│  ├─ 📄raidboss_emulator.png
│  ├─ 📄rdmty.png
│  ├─ 📄test.png
│  └─ 📄xephero.png
├─ 📁test
│  ├─ 📁helper
│  │  ├─ 📄example_log_lines_test_data.ts
│  │  ├─ 📄regex_util.ts
│  │  ├─ 📄test_data_runner.ts
│  │  ├─ 📄test_oopsy.ts
│  │  ├─ 📄test_timeline.ts
│  │  └─ 📄test_trigger.ts
│  ├─ 📁unittests
│  │  ├─ 📄compile_test.ts
│  │  ├─ 📄config_test.ts
│  │  ├─ 📄csv_util_test.ts
│  │  ├─ 📄netregex_test.ts
│  │  ├─ 📄regex_test.ts
│  │  ├─ 📄resources_test.ts
│  │  ├─ 📄responses_test.ts
│  │  └─ 📄util_test.ts
│  └─ 📄test_data_files.ts
├─ 📁types
│  ├─ 📄data.d.ts
│  ├─ 📄event.d.ts
│  ├─ 📄images.d.ts
│  ├─ 📄job.d.ts
│  ├─ 📄manifest.d.ts
│  ├─ 📄net_fields.d.ts
│  ├─ 📄net_matches.d.ts
│  ├─ 📄net_props.d.ts
│  ├─ 📄net_trigger.d.ts
│  ├─ 📄oopsy.d.ts
│  ├─ 📄party.d.ts
│  ├─ 📄trigger.d.ts
│  └─ 📄worker.d.ts
├─ 📁ui
│  ├─ 📁config
│  │  ├─ 📄config.css
│  │  ├─ 📄config.html
│  │  ├─ 📄config.ts
│  │  ├─ 📄config_options.ts
│  │  └─ 📄general_config.ts
│  ├─ 📁dps
│  │  ├─ 📁rdmty
│  │  │  ├─ 📄README.txt
│  │  │  ├─ 📄dps.css
│  │  │  ├─ 📄dps.html
│  │  │  └─ 📄dps.js
│  │  ├─ 📁xephero
│  │  │  ├─ 📄dps_phase_tracker.js
│  │  │  ├─ 📄xephero-cactbot.html
│  │  │  ├─ 📄xephero.css
│  │  │  └─ 📄xephero.js
│  │  └─ 📄dps_common.js
│  ├─ 📁eureka
│  │  ├─ 📄anemos.png
│  │  ├─ 📄bozjasouthern.png
│  │  ├─ 📄eureka.css
│  │  ├─ 📄eureka.html
│  │  ├─ 📄eureka.ts
│  │  ├─ 📄eureka_config.ts
│  │  ├─ 📄eureka_options.ts
│  │  ├─ 📄eureka_translations.ts
│  │  ├─ 📄hydatos.png
│  │  ├─ 📄pagos.png
│  │  ├─ 📄pyros.png
│  │  ├─ 📄zadnor.png
│  │  ├─ 📄zone_anemos.ts
│  │  ├─ 📄zone_bozja_southern.ts
│  │  ├─ 📄zone_hydatos.ts
│  │  ├─ 📄zone_pagos.ts
│  │  ├─ 📄zone_pyros.ts
│  │  └─ 📄zone_zadnor.ts
│  ├─ 📁jobs
│  │  ├─ 📁components
│  │  │  ├─ 📄ast.ts
│  │  │  ├─ 📄base.ts
│  │  │  ├─ 📄blm.ts
│  │  │  ├─ 📄blu.ts
│  │  │  ├─ 📄brd.ts
│  │  │  ├─ 📄dnc.ts
│  │  │  ├─ 📄drg.ts
│  │  │  ├─ 📄drk.ts
│  │  │  ├─ 📄gnb.ts
│  │  │  ├─ 📄index.ts
│  │  │  ├─ 📄mch.ts
│  │  │  ├─ 📄mnk.ts
│  │  │  ├─ 📄nin.ts
│  │  │  ├─ 📄pct.ts
│  │  │  ├─ 📄pld.ts
│  │  │  ├─ 📄rdm.ts
│  │  │  ├─ 📄rpr.ts
│  │  │  ├─ 📄sam.ts
│  │  │  ├─ 📄sch.ts
│  │  │  ├─ 📄sge.ts
│  │  │  ├─ 📄smn.ts
│  │  │  ├─ 📄vpr.ts
│  │  │  ├─ 📄war.ts
│  │  │  └─ 📄whm.ts
│  │  ├─ 📄bars.ts
│  │  ├─ 📄buff_tracker.ts
│  │  ├─ 📄combo_tracker.ts
│  │  ├─ 📄constants.ts
│  │  ├─ 📄event_emitter.ts
│  │  ├─ 📄jobs.css
│  │  ├─ 📄jobs.html
│  │  ├─ 📄jobs.ts
│  │  ├─ 📄jobs_config.ts
│  │  ├─ 📄jobs_options.ts
│  │  ├─ 📄player.ts
│  │  └─ 📄utils.ts
│  ├─ 📁oopsyraidsy
│  │  ├─ 📁data
│  │  │  ├─ 📁00-misc
│  │  │  │  ├─ 📄general.ts
│  │  │  │  └─ 📄test.ts
│  │  │  ├─ 📁02-arr
│  │  │  │  └─ 📁trial
│  │  │  │     ├─ 📄ifrit-nm.ts
│  │  │  │     ├─ 📄levi-ex.ts
│  │  │  │     ├─ 📄shiva-ex.ts
│  │  │  │     ├─ 📄shiva-hm.ts
│  │  │  │     ├─ 📄titan-ex.ts
│  │  │  │     ├─ 📄titan-hm.ts
│  │  │  │     ├─ 📄titan-nm.ts
│  │  │  │     └─ 📄ultima-ex.ts
│  │  │  ├─ 📁03-hw
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  └─ 📄weeping_city.ts
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄aetherochemical_research_facility.ts
│  │  │  │  │  ├─ 📄baelsars_wall.ts
│  │  │  │  │  ├─ 📄fractal_continuum.ts
│  │  │  │  │  ├─ 📄gubal_library_hard.ts
│  │  │  │  │  ├─ 📄sohm_al_hard.ts
│  │  │  │  │  └─ 📄the_lost_city_of_amdapor_hard.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄a10n.ts
│  │  │  │  │  ├─ 📄a12n.ts
│  │  │  │  │  ├─ 📄a3n.ts
│  │  │  │  │  └─ 📄a6n.ts
│  │  │  │  └─ 📁trial
│  │  │  │     ├─ 📄sephirot-ex.ts
│  │  │  │     ├─ 📄sophia-ex.ts
│  │  │  │     ├─ 📄thordan-ex.ts
│  │  │  │     └─ 📄zurvan-ex.ts
│  │  │  ├─ 📁04-sb
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  ├─ 📄orbonne_monastery.ts
│  │  │  │  │  ├─ 📄ridorana_lighthouse.ts
│  │  │  │  │  └─ 📄royal_city_of_rabanastre.ts
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄ala_mhigo.ts
│  │  │  │  │  ├─ 📄bardams_mettle.ts
│  │  │  │  │  ├─ 📄castrum_abania.ts
│  │  │  │  │  ├─ 📄doma_castle.ts
│  │  │  │  │  ├─ 📄drowned_city_of_skalla.ts
│  │  │  │  │  ├─ 📄fractal_continuum_hard.ts
│  │  │  │  │  ├─ 📄ghimlyt_dark.ts
│  │  │  │  │  ├─ 📄hells_lid.ts
│  │  │  │  │  ├─ 📄kugane_castle.ts
│  │  │  │  │  ├─ 📄shisui_of_the_violet_tides.ts
│  │  │  │  │  ├─ 📄sirensong_sea.ts
│  │  │  │  │  ├─ 📄st_mocianne_hard.ts
│  │  │  │  │  ├─ 📄swallows_compass.ts
│  │  │  │  │  ├─ 📄temple_of_the_fist.ts
│  │  │  │  │  └─ 📄the_burn.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄o10n.ts
│  │  │  │  │  ├─ 📄o10s.ts
│  │  │  │  │  ├─ 📄o11n.ts
│  │  │  │  │  ├─ 📄o11s.ts
│  │  │  │  │  ├─ 📄o12n.ts
│  │  │  │  │  ├─ 📄o12s.ts
│  │  │  │  │  ├─ 📄o1n.ts
│  │  │  │  │  ├─ 📄o1s.ts
│  │  │  │  │  ├─ 📄o2n.ts
│  │  │  │  │  ├─ 📄o2s.ts
│  │  │  │  │  ├─ 📄o3n.ts
│  │  │  │  │  ├─ 📄o3s.ts
│  │  │  │  │  ├─ 📄o4n.ts
│  │  │  │  │  ├─ 📄o4s.ts
│  │  │  │  │  ├─ 📄o5n.ts
│  │  │  │  │  ├─ 📄o5s.ts
│  │  │  │  │  ├─ 📄o6n.ts
│  │  │  │  │  ├─ 📄o6s.ts
│  │  │  │  │  ├─ 📄o7n.ts
│  │  │  │  │  ├─ 📄o7s.ts
│  │  │  │  │  ├─ 📄o8n.ts
│  │  │  │  │  ├─ 📄o8s.ts
│  │  │  │  │  ├─ 📄o9n.ts
│  │  │  │  │  └─ 📄o9s.ts
│  │  │  │  ├─ 📁trial
│  │  │  │  │  ├─ 📄byakko-ex.ts
│  │  │  │  │  ├─ 📄byakko.ts
│  │  │  │  │  ├─ 📄lakshmi-ex.ts
│  │  │  │  │  ├─ 📄lakshmi.ts
│  │  │  │  │  ├─ 📄rathalos-ex.ts
│  │  │  │  │  ├─ 📄rathalos.ts
│  │  │  │  │  ├─ 📄seiryu-ex.ts
│  │  │  │  │  ├─ 📄seiryu.ts
│  │  │  │  │  ├─ 📄shinryu-ex.ts
│  │  │  │  │  ├─ 📄shinryu.ts
│  │  │  │  │  ├─ 📄susano-ex.ts
│  │  │  │  │  ├─ 📄susano.ts
│  │  │  │  │  ├─ 📄suzaku-ex.ts
│  │  │  │  │  ├─ 📄suzaku.ts
│  │  │  │  │  ├─ 📄tsukuyomi-ex.ts
│  │  │  │  │  ├─ 📄tsukuyomi.ts
│  │  │  │  │  └─ 📄yojimbo.ts
│  │  │  │  └─ 📁ultimate
│  │  │  │     ├─ 📄ultima_weapon_ultimate.ts
│  │  │  │     └─ 📄unending_coil_ultimate.ts
│  │  │  ├─ 📁05-shb
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  ├─ 📄the_copied_factory.ts
│  │  │  │  │  ├─ 📄the_puppets_bunker.ts
│  │  │  │  │  └─ 📄the_tower_at_paradigms_breach.ts
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄akadaemia_anyder.ts
│  │  │  │  │  ├─ 📄amaurot.ts
│  │  │  │  │  ├─ 📄anamnesis_anyder.ts
│  │  │  │  │  ├─ 📄dohn_mheg.ts
│  │  │  │  │  ├─ 📄heroes_gauntlet.ts
│  │  │  │  │  ├─ 📄holminster_switch.ts
│  │  │  │  │  ├─ 📄malikahs_well.ts
│  │  │  │  │  ├─ 📄matoyas_relict.ts
│  │  │  │  │  ├─ 📄mt_gulg.ts
│  │  │  │  │  ├─ 📄paglthan.ts
│  │  │  │  │  ├─ 📄qitana_ravel.ts
│  │  │  │  │  ├─ 📄the_grand_cosmos.ts
│  │  │  │  │  └─ 📄twinning.ts
│  │  │  │  ├─ 📁eureka
│  │  │  │  │  ├─ 📄delubrum_reginae.ts
│  │  │  │  │  └─ 📄delubrum_reginae_savage.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄e10n.ts
│  │  │  │  │  ├─ 📄e10s.ts
│  │  │  │  │  ├─ 📄e11n.ts
│  │  │  │  │  ├─ 📄e11s.ts
│  │  │  │  │  ├─ 📄e12n.ts
│  │  │  │  │  ├─ 📄e12s.ts
│  │  │  │  │  ├─ 📄e1n.ts
│  │  │  │  │  ├─ 📄e1s.ts
│  │  │  │  │  ├─ 📄e2n.ts
│  │  │  │  │  ├─ 📄e2s.ts
│  │  │  │  │  ├─ 📄e3n.ts
│  │  │  │  │  ├─ 📄e3s.ts
│  │  │  │  │  ├─ 📄e4n.ts
│  │  │  │  │  ├─ 📄e4s.ts
│  │  │  │  │  ├─ 📄e5n.ts
│  │  │  │  │  ├─ 📄e5s.ts
│  │  │  │  │  ├─ 📄e6n.ts
│  │  │  │  │  ├─ 📄e6s.ts
│  │  │  │  │  ├─ 📄e7n.ts
│  │  │  │  │  ├─ 📄e7s.ts
│  │  │  │  │  ├─ 📄e8n.ts
│  │  │  │  │  ├─ 📄e8s.ts
│  │  │  │  │  ├─ 📄e9n.ts
│  │  │  │  │  └─ 📄e9s.ts
│  │  │  │  ├─ 📁trial
│  │  │  │  │  ├─ 📄diamond_weapon-ex.ts
│  │  │  │  │  ├─ 📄diamond_weapon.ts
│  │  │  │  │  ├─ 📄emerald_weapon-ex.ts
│  │  │  │  │  ├─ 📄emerald_weapon.ts
│  │  │  │  │  ├─ 📄hades-ex.ts
│  │  │  │  │  ├─ 📄hades.ts
│  │  │  │  │  ├─ 📄innocence-ex.ts
│  │  │  │  │  ├─ 📄innocence.ts
│  │  │  │  │  ├─ 📄levi-un.ts
│  │  │  │  │  ├─ 📄ruby_weapon-ex.ts
│  │  │  │  │  ├─ 📄ruby_weapon.ts
│  │  │  │  │  ├─ 📄shiva-un.ts
│  │  │  │  │  ├─ 📄titan-un.ts
│  │  │  │  │  ├─ 📄titania-ex.ts
│  │  │  │  │  ├─ 📄titania.ts
│  │  │  │  │  ├─ 📄varis-ex.ts
│  │  │  │  │  ├─ 📄wol-ex.ts
│  │  │  │  │  └─ 📄wol.ts
│  │  │  │  └─ 📁ultimate
│  │  │  │     └─ 📄the_epic_of_alexander.ts
│  │  │  ├─ 📁06-ew
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  ├─ 📄aglaia.ts
│  │  │  │  │  ├─ 📄euphrosyne.ts
│  │  │  │  │  └─ 📄thaleia.ts
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄aetherfont.ts
│  │  │  │  │  ├─ 📄aloalo_island.ts
│  │  │  │  │  ├─ 📄alzadaals_legacy.ts
│  │  │  │  │  ├─ 📄another_aloalo_island-savage.ts
│  │  │  │  │  ├─ 📄another_aloalo_island.ts
│  │  │  │  │  ├─ 📄another_mount_rokkon-savage.ts
│  │  │  │  │  ├─ 📄another_mount_rokkon.ts
│  │  │  │  │  ├─ 📄another_sildihn_subterrane-savage.ts
│  │  │  │  │  ├─ 📄another_sildihn_subterrane.ts
│  │  │  │  │  ├─ 📄ktisis_hyperboreia.ts
│  │  │  │  │  ├─ 📄lapis_manalis.ts
│  │  │  │  │  ├─ 📄mount_rokkon.ts
│  │  │  │  │  ├─ 📄smileton.ts
│  │  │  │  │  ├─ 📄stigma_dreamscape.ts
│  │  │  │  │  ├─ 📄the_aitiascope.ts
│  │  │  │  │  ├─ 📄the_dead_ends.ts
│  │  │  │  │  ├─ 📄the_fell_court_of_troia.ts
│  │  │  │  │  ├─ 📄the_lunar_subteranne.ts
│  │  │  │  │  ├─ 📄the_sildihn_subterrane.ts
│  │  │  │  │  ├─ 📄the_tower_of_babil.ts
│  │  │  │  │  ├─ 📄the_tower_of_zot.ts
│  │  │  │  │  └─ 📄vanaspati.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄p10n.ts
│  │  │  │  │  ├─ 📄p10s.ts
│  │  │  │  │  ├─ 📄p11n.ts
│  │  │  │  │  ├─ 📄p11s.ts
│  │  │  │  │  ├─ 📄p12n.ts
│  │  │  │  │  ├─ 📄p12s.ts
│  │  │  │  │  ├─ 📄p1n.ts
│  │  │  │  │  ├─ 📄p1s.ts
│  │  │  │  │  ├─ 📄p2n.ts
│  │  │  │  │  ├─ 📄p2s.ts
│  │  │  │  │  ├─ 📄p3n.ts
│  │  │  │  │  ├─ 📄p3s.ts
│  │  │  │  │  ├─ 📄p4n.ts
│  │  │  │  │  ├─ 📄p4s.ts
│  │  │  │  │  ├─ 📄p5n.ts
│  │  │  │  │  ├─ 📄p5s.ts
│  │  │  │  │  ├─ 📄p6n.ts
│  │  │  │  │  ├─ 📄p6s.ts
│  │  │  │  │  ├─ 📄p7n.ts
│  │  │  │  │  ├─ 📄p7s.ts
│  │  │  │  │  ├─ 📄p8n.ts
│  │  │  │  │  ├─ 📄p8s.ts
│  │  │  │  │  ├─ 📄p9n.ts
│  │  │  │  │  └─ 📄p9s.ts
│  │  │  │  ├─ 📁trial
│  │  │  │  │  ├─ 📄asura.ts
│  │  │  │  │  ├─ 📄barbariccia-ex.ts
│  │  │  │  │  ├─ 📄barbariccia.ts
│  │  │  │  │  ├─ 📄endsinger-ex.ts
│  │  │  │  │  ├─ 📄endsinger.ts
│  │  │  │  │  ├─ 📄golbez-ex.ts
│  │  │  │  │  ├─ 📄golbez.ts
│  │  │  │  │  ├─ 📄hydaelyn-ex.ts
│  │  │  │  │  ├─ 📄hydaelyn.ts
│  │  │  │  │  ├─ 📄rubicante-ex.ts
│  │  │  │  │  ├─ 📄rubicante.ts
│  │  │  │  │  ├─ 📄sephirot-un.ts
│  │  │  │  │  ├─ 📄sophia-un.ts
│  │  │  │  │  ├─ 📄thordan-un.ts
│  │  │  │  │  ├─ 📄ultima-un.ts
│  │  │  │  │  ├─ 📄zeromus-ex.ts
│  │  │  │  │  ├─ 📄zeromus.ts
│  │  │  │  │  ├─ 📄zodiark-ex.ts
│  │  │  │  │  ├─ 📄zodiark.ts
│  │  │  │  │  └─ 📄zurvan-un.ts
│  │  │  │  └─ 📁ultimate
│  │  │  │     ├─ 📄dragonsongs_reprise_ultimate.ts
│  │  │  │     └─ 📄the_omega_protocol.ts
│  │  │  ├─ 📁07-dt
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  └─ 📄jeuno-first-walk.ts
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄alexandria.ts
│  │  │  │  │  ├─ 📄ihuykatumu.ts
│  │  │  │  │  ├─ 📄origenics.ts
│  │  │  │  │  ├─ 📄skydeep-cenote.ts
│  │  │  │  │  ├─ 📄strayborough-deadwalk.ts
│  │  │  │  │  ├─ 📄vanguard.ts
│  │  │  │  │  ├─ 📄worqor-zormor.ts
│  │  │  │  │  └─ 📄yuweyawata.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄r1n.ts
│  │  │  │  │  ├─ 📄r1s.ts
│  │  │  │  │  ├─ 📄r2n.ts
│  │  │  │  │  ├─ 📄r2s.ts
│  │  │  │  │  ├─ 📄r3n.ts
│  │  │  │  │  ├─ 📄r3s.ts
│  │  │  │  │  ├─ 📄r4n.ts
│  │  │  │  │  └─ 📄r4s.ts
│  │  │  │  ├─ 📁trial
│  │  │  │  │  ├─ 📄byakko-un.ts
│  │  │  │  │  ├─ 📄valigarmanda-ex.ts
│  │  │  │  │  ├─ 📄valigarmanda.ts
│  │  │  │  │  ├─ 📄zoraal-ja-ex.ts
│  │  │  │  │  └─ 📄zoraal-ja.ts
│  │  │  │  └─ 📁ultimate
│  │  │  │     └─ 📄futures_rewritten.ts
│  │  │  └─ 📄oopsy_manifest.txt
│  │  ├─ 📄buff_map.ts
│  │  ├─ 📄combat_state.ts
│  │  ├─ 📄damage_tracker.ts
│  │  ├─ 📄death_report.ts
│  │  ├─ 📄missed_buff_collector.ts
│  │  ├─ 📄mistake_collector.ts
│  │  ├─ 📄mistake_observer.ts
│  │  ├─ 📄oopsy_common.css
│  │  ├─ 📄oopsy_common.ts
│  │  ├─ 📄oopsy_fields.ts
│  │  ├─ 📄oopsy_live.css
│  │  ├─ 📄oopsy_live.ts
│  │  ├─ 📄oopsy_live_list.ts
│  │  ├─ 📄oopsy_options.ts
│  │  ├─ 📄oopsy_summary.css
│  │  ├─ 📄oopsy_summary.html
│  │  ├─ 📄oopsy_summary.ts
│  │  ├─ 📄oopsy_summary_list.ts
│  │  ├─ 📄oopsy_viewer.css
│  │  ├─ 📄oopsy_viewer.html
│  │  ├─ 📄oopsy_viewer.ts
│  │  ├─ 📄oopsyraidsy.html
│  │  ├─ 📄oopsyraidsy.ts
│  │  ├─ 📄oopsyraidsy_config.ts
│  │  └─ 📄player_state_tracker.ts
│  ├─ 📁pullcounter
│  │  ├─ 📄pullcounter.css
│  │  ├─ 📄pullcounter.html
│  │  └─ 📄pullcounter.ts
│  ├─ 📁radar
│  │  ├─ 📄arrow.png
│  │  ├─ 📄radar.css
│  │  ├─ 📄radar.html
│  │  ├─ 📄radar.ts
│  │  └─ 📄radar_config.ts
│  ├─ 📁raidboss
│  │  ├─ 📁data
│  │  │  ├─ 📁00-misc
│  │  │  │  ├─ 📄general.ts
│  │  │  │  ├─ 📄test.ts
│  │  │  │  ├─ 📄test.txt
│  │  │  │  └─ 📄the_masked_carnivale.ts
│  │  │  ├─ 📁02-arr
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  └─ 📄the_world_of_darkness.ts
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄amdapor_keep.ts
│  │  │  │  │  ├─ 📄amdapor_keep_hard.ts
│  │  │  │  │  ├─ 📄aurum_vale.ts
│  │  │  │  │  ├─ 📄brayfloxs_longstop.ts
│  │  │  │  │  ├─ 📄cutters_cry.ts
│  │  │  │  │  ├─ 📄halatali_hard.ts
│  │  │  │  │  ├─ 📄haukke_manor.ts
│  │  │  │  │  ├─ 📄haukke_manor_hard.ts
│  │  │  │  │  ├─ 📄hullbreaker_isle.ts
│  │  │  │  │  ├─ 📄pharos_sirius.ts
│  │  │  │  │  ├─ 📄sastasha_hard.ts
│  │  │  │  │  ├─ 📄snowcloak.ts
│  │  │  │  │  ├─ 📄the_lost_city_of_amdapor.ts
│  │  │  │  │  ├─ 📄the_stone_vigil.ts
│  │  │  │  │  ├─ 📄the_stone_vigil_hard.ts
│  │  │  │  │  ├─ 📄the_sunken_temple_of_quarn.ts
│  │  │  │  │  ├─ 📄the_sunken_temple_of_quarn_hard.ts
│  │  │  │  │  ├─ 📄the_tam_tara_depocraft_hard.ts
│  │  │  │  │  └─ 📄the_wanderers_palace_hard.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄t1.ts
│  │  │  │  │  ├─ 📄t10.ts
│  │  │  │  │  ├─ 📄t10.txt
│  │  │  │  │  ├─ 📄t11.ts
│  │  │  │  │  ├─ 📄t11.txt
│  │  │  │  │  ├─ 📄t12.ts
│  │  │  │  │  ├─ 📄t12.txt
│  │  │  │  │  ├─ 📄t13.ts
│  │  │  │  │  ├─ 📄t13.txt
│  │  │  │  │  ├─ 📄t2.ts
│  │  │  │  │  ├─ 📄t4.ts
│  │  │  │  │  ├─ 📄t4.txt
│  │  │  │  │  ├─ 📄t5.ts
│  │  │  │  │  ├─ 📄t5.txt
│  │  │  │  │  ├─ 📄t6.ts
│  │  │  │  │  ├─ 📄t6.txt
│  │  │  │  │  ├─ 📄t7.ts
│  │  │  │  │  ├─ 📄t7.txt
│  │  │  │  │  ├─ 📄t8.ts
│  │  │  │  │  ├─ 📄t8.txt
│  │  │  │  │  ├─ 📄t9.ts
│  │  │  │  │  └─ 📄t9.txt
│  │  │  │  └─ 📁trial
│  │  │  │     ├─ 📄ifrit-nm.ts
│  │  │  │     ├─ 📄ifrit-nm.txt
│  │  │  │     ├─ 📄levi-ex.ts
│  │  │  │     ├─ 📄levi-ex.txt
│  │  │  │     ├─ 📄shiva-ex.ts
│  │  │  │     ├─ 📄shiva-ex.txt
│  │  │  │     ├─ 📄shiva-hm.ts
│  │  │  │     ├─ 📄shiva-hm.txt
│  │  │  │     ├─ 📄titan-ex.ts
│  │  │  │     ├─ 📄titan-ex.txt
│  │  │  │     ├─ 📄titan-hm.ts
│  │  │  │     ├─ 📄titan-hm.txt
│  │  │  │     ├─ 📄titan-nm.ts
│  │  │  │     ├─ 📄titan-nm.txt
│  │  │  │     ├─ 📄ultima-ex.ts
│  │  │  │     └─ 📄ultima-ex.txt
│  │  │  ├─ 📁03-hw
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  ├─ 📄dun_scaith.ts
│  │  │  │  │  ├─ 📄dun_scaith.txt
│  │  │  │  │  ├─ 📄weeping_city.ts
│  │  │  │  │  └─ 📄weeping_city.txt
│  │  │  │  ├─ 📁deepdungeon
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_001-010.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_011-020.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_021-030.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_031-040.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_041-050.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_051-060.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_061-070.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_071-080.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_081-090.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_091-100.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_101-110.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_111-120.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_121-130.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_131-140.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_141-150.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_151-160.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_161-170.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_171-180.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_181-190.ts
│  │  │  │  │  ├─ 📄the_palace_of_the_dead_floors_191-200.ts
│  │  │  │  │  └─ 📄the_palace_of_the_dead_general.ts
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄aetherochemical_research_facility.ts
│  │  │  │  │  ├─ 📄aetherochemical_research_facility.txt
│  │  │  │  │  ├─ 📄baelsars_wall.ts
│  │  │  │  │  ├─ 📄baelsars_wall.txt
│  │  │  │  │  ├─ 📄fractal_continuum.ts
│  │  │  │  │  ├─ 📄fractal_continuum.txt
│  │  │  │  │  ├─ 📄gubal_library_hard.ts
│  │  │  │  │  ├─ 📄gubal_library_hard.txt
│  │  │  │  │  ├─ 📄sohm_al.ts
│  │  │  │  │  ├─ 📄sohm_al.txt
│  │  │  │  │  ├─ 📄sohm_al_hard.ts
│  │  │  │  │  ├─ 📄sohm_al_hard.txt
│  │  │  │  │  ├─ 📄the_lost_city_of_amdapor_hard.ts
│  │  │  │  │  ├─ 📄the_lost_city_of_amdapor_hard.txt
│  │  │  │  │  ├─ 📄the_vault.ts
│  │  │  │  │  ├─ 📄the_vault.txt
│  │  │  │  │  ├─ 📄xelphatol.ts
│  │  │  │  │  └─ 📄xelphatol.txt
│  │  │  │  ├─ 📁map
│  │  │  │  │  └─ 📄the_aquapolis.ts
│  │  │  │  ├─ 📁pvp
│  │  │  │  │  └─ 📄shatter.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄a10n.ts
│  │  │  │  │  ├─ 📄a10n.txt
│  │  │  │  │  ├─ 📄a10s.ts
│  │  │  │  │  ├─ 📄a10s.txt
│  │  │  │  │  ├─ 📄a11s.ts
│  │  │  │  │  ├─ 📄a11s.txt
│  │  │  │  │  ├─ 📄a12n.ts
│  │  │  │  │  ├─ 📄a12n.txt
│  │  │  │  │  ├─ 📄a12s.ts
│  │  │  │  │  ├─ 📄a12s.txt
│  │  │  │  │  ├─ 📄a1s.ts
│  │  │  │  │  ├─ 📄a1s.txt
│  │  │  │  │  ├─ 📄a2s.ts
│  │  │  │  │  ├─ 📄a2s.txt
│  │  │  │  │  ├─ 📄a3n.ts
│  │  │  │  │  ├─ 📄a3n.txt
│  │  │  │  │  ├─ 📄a3s.ts
│  │  │  │  │  ├─ 📄a3s.txt
│  │  │  │  │  ├─ 📄a4s.ts
│  │  │  │  │  ├─ 📄a4s.txt
│  │  │  │  │  ├─ 📄a5s.ts
│  │  │  │  │  ├─ 📄a5s.txt
│  │  │  │  │  ├─ 📄a6n.ts
│  │  │  │  │  ├─ 📄a6n.txt
│  │  │  │  │  ├─ 📄a6s.ts
│  │  │  │  │  ├─ 📄a6s.txt
│  │  │  │  │  ├─ 📄a7s.ts
│  │  │  │  │  ├─ 📄a7s.txt
│  │  │  │  │  ├─ 📄a8n.ts
│  │  │  │  │  ├─ 📄a8n.txt
│  │  │  │  │  ├─ 📄a8s.ts
│  │  │  │  │  ├─ 📄a8s.txt
│  │  │  │  │  ├─ 📄a9s.ts
│  │  │  │  │  └─ 📄a9s.txt
│  │  │  │  └─ 📁trial
│  │  │  │     ├─ 📄bismarck-ex.ts
│  │  │  │     ├─ 📄ravana-ex.ts
│  │  │  │     ├─ 📄ravana-ex.txt
│  │  │  │     ├─ 📄sephirot-ex.ts
│  │  │  │     ├─ 📄sephirot-ex.txt
│  │  │  │     ├─ 📄sephirot.ts
│  │  │  │     ├─ 📄sophia-ex.ts
│  │  │  │     ├─ 📄sophia-ex.txt
│  │  │  │     ├─ 📄thordan-ex.ts
│  │  │  │     ├─ 📄thordan-ex.txt
│  │  │  │     ├─ 📄zurvan-ex.ts
│  │  │  │     └─ 📄zurvan-ex.txt
│  │  │  ├─ 📁04-sb
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  ├─ 📄orbonne_monastery.ts
│  │  │  │  │  ├─ 📄orbonne_monastery.txt
│  │  │  │  │  ├─ 📄ridorana_lighthouse.ts
│  │  │  │  │  ├─ 📄ridorana_lighthouse.txt
│  │  │  │  │  ├─ 📄royal_city_of_rabanastre.ts
│  │  │  │  │  └─ 📄royal_city_of_rabanastre.txt
│  │  │  │  ├─ 📁deepdungeon
│  │  │  │  │  ├─ 📄heaven-on-high_floors_01-10.ts
│  │  │  │  │  ├─ 📄heaven-on-high_floors_11-20.ts
│  │  │  │  │  ├─ 📄heaven-on-high_floors_21-30.ts
│  │  │  │  │  ├─ 📄heaven-on-high_floors_31-40.ts
│  │  │  │  │  ├─ 📄heaven-on-high_floors_41-50.ts
│  │  │  │  │  ├─ 📄heaven-on-high_floors_51-60.ts
│  │  │  │  │  ├─ 📄heaven-on-high_floors_61-70.ts
│  │  │  │  │  ├─ 📄heaven-on-high_floors_71-80.ts
│  │  │  │  │  ├─ 📄heaven-on-high_floors_81-90.ts
│  │  │  │  │  ├─ 📄heaven-on-high_floors_91-100.ts
│  │  │  │  │  └─ 📄heaven-on-high_general.ts
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄ala_mhigo.ts
│  │  │  │  │  ├─ 📄ala_mhigo.txt
│  │  │  │  │  ├─ 📄bardams_mettle.ts
│  │  │  │  │  ├─ 📄bardams_mettle.txt
│  │  │  │  │  ├─ 📄castrum_abania.ts
│  │  │  │  │  ├─ 📄castrum_abania.txt
│  │  │  │  │  ├─ 📄doma_castle.ts
│  │  │  │  │  ├─ 📄doma_castle.txt
│  │  │  │  │  ├─ 📄drowned_city_of_skalla.ts
│  │  │  │  │  ├─ 📄drowned_city_of_skalla.txt
│  │  │  │  │  ├─ 📄fractal_continuum_hard.ts
│  │  │  │  │  ├─ 📄fractal_continuum_hard.txt
│  │  │  │  │  ├─ 📄ghimlyt_dark.ts
│  │  │  │  │  ├─ 📄ghimlyt_dark.txt
│  │  │  │  │  ├─ 📄hells_lid.ts
│  │  │  │  │  ├─ 📄hells_lid.txt
│  │  │  │  │  ├─ 📄kugane_castle.ts
│  │  │  │  │  ├─ 📄kugane_castle.txt
│  │  │  │  │  ├─ 📄shisui_of_the_violet_tides.ts
│  │  │  │  │  ├─ 📄shisui_of_the_violet_tides.txt
│  │  │  │  │  ├─ 📄sirensong_sea.ts
│  │  │  │  │  ├─ 📄sirensong_sea.txt
│  │  │  │  │  ├─ 📄st_mocianne_hard.ts
│  │  │  │  │  ├─ 📄st_mocianne_hard.txt
│  │  │  │  │  ├─ 📄swallows_compass.ts
│  │  │  │  │  ├─ 📄swallows_compass.txt
│  │  │  │  │  ├─ 📄temple_of_the_fist.ts
│  │  │  │  │  ├─ 📄temple_of_the_fist.txt
│  │  │  │  │  ├─ 📄the_burn.ts
│  │  │  │  │  └─ 📄the_burn.txt
│  │  │  │  ├─ 📁eureka
│  │  │  │  │  ├─ 📄eureka_anemos.ts
│  │  │  │  │  ├─ 📄eureka_hydatos.ts
│  │  │  │  │  ├─ 📄eureka_hydatos.txt
│  │  │  │  │  ├─ 📄eureka_pagos.ts
│  │  │  │  │  └─ 📄eureka_pyros.ts
│  │  │  │  ├─ 📁hunts
│  │  │  │  │  └─ 📄yanxia.ts
│  │  │  │  ├─ 📁map
│  │  │  │  │  ├─ 📄the_hidden_canals_of_uznair.ts
│  │  │  │  │  ├─ 📄the_lost_canals_of_uznair.ts
│  │  │  │  │  └─ 📄the_shifting_altars_of_uznair.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄o10n.ts
│  │  │  │  │  ├─ 📄o10n.txt
│  │  │  │  │  ├─ 📄o10s.ts
│  │  │  │  │  ├─ 📄o10s.txt
│  │  │  │  │  ├─ 📄o11n.ts
│  │  │  │  │  ├─ 📄o11n.txt
│  │  │  │  │  ├─ 📄o11s.ts
│  │  │  │  │  ├─ 📄o11s.txt
│  │  │  │  │  ├─ 📄o12n.ts
│  │  │  │  │  ├─ 📄o12n.txt
│  │  │  │  │  ├─ 📄o12s.ts
│  │  │  │  │  ├─ 📄o12s.txt
│  │  │  │  │  ├─ 📄o1n.ts
│  │  │  │  │  ├─ 📄o1n.txt
│  │  │  │  │  ├─ 📄o1s.ts
│  │  │  │  │  ├─ 📄o1s.txt
│  │  │  │  │  ├─ 📄o2n.ts
│  │  │  │  │  ├─ 📄o2n.txt
│  │  │  │  │  ├─ 📄o2s.ts
│  │  │  │  │  ├─ 📄o2s.txt
│  │  │  │  │  ├─ 📄o3n.ts
│  │  │  │  │  ├─ 📄o3n.txt
│  │  │  │  │  ├─ 📄o3s.ts
│  │  │  │  │  ├─ 📄o3s.txt
│  │  │  │  │  ├─ 📄o4n.ts
│  │  │  │  │  ├─ 📄o4n.txt
│  │  │  │  │  ├─ 📄o4s.ts
│  │  │  │  │  ├─ 📄o4s.txt
│  │  │  │  │  ├─ 📄o5n.ts
│  │  │  │  │  ├─ 📄o5n.txt
│  │  │  │  │  ├─ 📄o5s.ts
│  │  │  │  │  ├─ 📄o5s.txt
│  │  │  │  │  ├─ 📄o6n.ts
│  │  │  │  │  ├─ 📄o6n.txt
│  │  │  │  │  ├─ 📄o6s.ts
│  │  │  │  │  ├─ 📄o6s.txt
│  │  │  │  │  ├─ 📄o7n.ts
│  │  │  │  │  ├─ 📄o7n.txt
│  │  │  │  │  ├─ 📄o7s.ts
│  │  │  │  │  ├─ 📄o7s.txt
│  │  │  │  │  ├─ 📄o8n.ts
│  │  │  │  │  ├─ 📄o8n.txt
│  │  │  │  │  ├─ 📄o8s.ts
│  │  │  │  │  ├─ 📄o8s.txt
│  │  │  │  │  ├─ 📄o9n.ts
│  │  │  │  │  ├─ 📄o9n.txt
│  │  │  │  │  ├─ 📄o9s.ts
│  │  │  │  │  └─ 📄o9s.txt
│  │  │  │  ├─ 📁trial
│  │  │  │  │  ├─ 📄byakko-ex.ts
│  │  │  │  │  ├─ 📄byakko-ex.txt
│  │  │  │  │  ├─ 📄byakko.ts
│  │  │  │  │  ├─ 📄byakko.txt
│  │  │  │  │  ├─ 📄lakshmi-ex.ts
│  │  │  │  │  ├─ 📄lakshmi-ex.txt
│  │  │  │  │  ├─ 📄lakshmi.ts
│  │  │  │  │  ├─ 📄lakshmi.txt
│  │  │  │  │  ├─ 📄rathalos-ex.ts
│  │  │  │  │  ├─ 📄rathalos.ts
│  │  │  │  │  ├─ 📄seiryu-ex.ts
│  │  │  │  │  ├─ 📄seiryu-ex.txt
│  │  │  │  │  ├─ 📄seiryu.ts
│  │  │  │  │  ├─ 📄seiryu.txt
│  │  │  │  │  ├─ 📄shinryu-ex.ts
│  │  │  │  │  ├─ 📄shinryu-ex.txt
│  │  │  │  │  ├─ 📄shinryu.ts
│  │  │  │  │  ├─ 📄shinryu.txt
│  │  │  │  │  ├─ 📄susano-ex.ts
│  │  │  │  │  ├─ 📄susano-ex.txt
│  │  │  │  │  ├─ 📄susano.ts
│  │  │  │  │  ├─ 📄susano.txt
│  │  │  │  │  ├─ 📄suzaku-ex.ts
│  │  │  │  │  ├─ 📄suzaku-ex.txt
│  │  │  │  │  ├─ 📄suzaku.ts
│  │  │  │  │  ├─ 📄suzaku.txt
│  │  │  │  │  ├─ 📄tsukuyomi-ex.ts
│  │  │  │  │  ├─ 📄tsukuyomi-ex.txt
│  │  │  │  │  ├─ 📄tsukuyomi.ts
│  │  │  │  │  ├─ 📄tsukuyomi.txt
│  │  │  │  │  ├─ 📄yojimbo.ts
│  │  │  │  │  └─ 📄yojimbo.txt
│  │  │  │  └─ 📁ultimate
│  │  │  │     ├─ 📄ultima_weapon_ultimate.ts
│  │  │  │     ├─ 📄ultima_weapon_ultimate.txt
│  │  │  │     ├─ 📄unending_coil_ultimate.ts
│  │  │  │     └─ 📄unending_coil_ultimate.txt
│  │  │  ├─ 📁05-shb
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  ├─ 📄the_copied_factory.ts
│  │  │  │  │  ├─ 📄the_copied_factory.txt
│  │  │  │  │  ├─ 📄the_puppets_bunker.ts
│  │  │  │  │  ├─ 📄the_puppets_bunker.txt
│  │  │  │  │  ├─ 📄the_tower_at_paradigms_breach.ts
│  │  │  │  │  └─ 📄the_tower_at_paradigms_breach.txt
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄akadaemia_anyder.ts
│  │  │  │  │  ├─ 📄akadaemia_anyder.txt
│  │  │  │  │  ├─ 📄amaurot.ts
│  │  │  │  │  ├─ 📄amaurot.txt
│  │  │  │  │  ├─ 📄anamnesis_anyder.ts
│  │  │  │  │  ├─ 📄anamnesis_anyder.txt
│  │  │  │  │  ├─ 📄dohn_mheg.ts
│  │  │  │  │  ├─ 📄dohn_mheg.txt
│  │  │  │  │  ├─ 📄heroes_gauntlet.ts
│  │  │  │  │  ├─ 📄heroes_gauntlet.txt
│  │  │  │  │  ├─ 📄holminster_switch.ts
│  │  │  │  │  ├─ 📄holminster_switch.txt
│  │  │  │  │  ├─ 📄malikahs_well.ts
│  │  │  │  │  ├─ 📄malikahs_well.txt
│  │  │  │  │  ├─ 📄matoyas_relict.ts
│  │  │  │  │  ├─ 📄matoyas_relict.txt
│  │  │  │  │  ├─ 📄mt_gulg.ts
│  │  │  │  │  ├─ 📄mt_gulg.txt
│  │  │  │  │  ├─ 📄paglthan.ts
│  │  │  │  │  ├─ 📄paglthan.txt
│  │  │  │  │  ├─ 📄qitana_ravel.ts
│  │  │  │  │  ├─ 📄qitana_ravel.txt
│  │  │  │  │  ├─ 📄the_grand_cosmos.ts
│  │  │  │  │  ├─ 📄the_grand_cosmos.txt
│  │  │  │  │  ├─ 📄twinning.ts
│  │  │  │  │  └─ 📄twinning.txt
│  │  │  │  ├─ 📁etc
│  │  │  │  │  └─ 📄the_diadem.ts
│  │  │  │  ├─ 📁eureka
│  │  │  │  │  ├─ 📄bozjan_southern_front.ts
│  │  │  │  │  ├─ 📄bozjan_southern_front.txt
│  │  │  │  │  ├─ 📄delubrum_reginae.ts
│  │  │  │  │  ├─ 📄delubrum_reginae.txt
│  │  │  │  │  ├─ 📄delubrum_reginae_savage.ts
│  │  │  │  │  ├─ 📄delubrum_reginae_savage.txt
│  │  │  │  │  ├─ 📄zadnor.ts
│  │  │  │  │  └─ 📄zadnor.txt
│  │  │  │  ├─ 📁hunts
│  │  │  │  │  ├─ 📄amh_araeng.ts
│  │  │  │  │  ├─ 📄il_mheg.ts
│  │  │  │  │  ├─ 📄kholusia.ts
│  │  │  │  │  ├─ 📄lakeland.ts
│  │  │  │  │  ├─ 📄ss_rank.ts
│  │  │  │  │  ├─ 📄the_raktika_greatwood.ts
│  │  │  │  │  └─ 📄the_tempest.ts
│  │  │  │  ├─ 📁map
│  │  │  │  │  ├─ 📄the_dungeons_of_lyhe_ghiah.ts
│  │  │  │  │  └─ 📄the_shifting_oubliettes_of_lyhe_ghiah.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄e10n.ts
│  │  │  │  │  ├─ 📄e10n.txt
│  │  │  │  │  ├─ 📄e10s.ts
│  │  │  │  │  ├─ 📄e10s.txt
│  │  │  │  │  ├─ 📄e11n.ts
│  │  │  │  │  ├─ 📄e11n.txt
│  │  │  │  │  ├─ 📄e11s.ts
│  │  │  │  │  ├─ 📄e11s.txt
│  │  │  │  │  ├─ 📄e12n.ts
│  │  │  │  │  ├─ 📄e12n.txt
│  │  │  │  │  ├─ 📄e12s.ts
│  │  │  │  │  ├─ 📄e12s.txt
│  │  │  │  │  ├─ 📄e1n.ts
│  │  │  │  │  ├─ 📄e1n.txt
│  │  │  │  │  ├─ 📄e1s.ts
│  │  │  │  │  ├─ 📄e1s.txt
│  │  │  │  │  ├─ 📄e2n.ts
│  │  │  │  │  ├─ 📄e2n.txt
│  │  │  │  │  ├─ 📄e2s.ts
│  │  │  │  │  ├─ 📄e2s.txt
│  │  │  │  │  ├─ 📄e3n.ts
│  │  │  │  │  ├─ 📄e3n.txt
│  │  │  │  │  ├─ 📄e3s.ts
│  │  │  │  │  ├─ 📄e3s.txt
│  │  │  │  │  ├─ 📄e4n.ts
│  │  │  │  │  ├─ 📄e4n.txt
│  │  │  │  │  ├─ 📄e4s.ts
│  │  │  │  │  ├─ 📄e4s.txt
│  │  │  │  │  ├─ 📄e5n.ts
│  │  │  │  │  ├─ 📄e5n.txt
│  │  │  │  │  ├─ 📄e5s.ts
│  │  │  │  │  ├─ 📄e5s.txt
│  │  │  │  │  ├─ 📄e6n.ts
│  │  │  │  │  ├─ 📄e6n.txt
│  │  │  │  │  ├─ 📄e6s.ts
│  │  │  │  │  ├─ 📄e6s.txt
│  │  │  │  │  ├─ 📄e7n.ts
│  │  │  │  │  ├─ 📄e7n.txt
│  │  │  │  │  ├─ 📄e7s.ts
│  │  │  │  │  ├─ 📄e7s.txt
│  │  │  │  │  ├─ 📄e8n.ts
│  │  │  │  │  ├─ 📄e8n.txt
│  │  │  │  │  ├─ 📄e8s.ts
│  │  │  │  │  ├─ 📄e8s.txt
│  │  │  │  │  ├─ 📄e9n.ts
│  │  │  │  │  ├─ 📄e9n.txt
│  │  │  │  │  ├─ 📄e9s.ts
│  │  │  │  │  └─ 📄e9s.txt
│  │  │  │  ├─ 📁trial
│  │  │  │  │  ├─ 📄diamond_weapon-ex.ts
│  │  │  │  │  ├─ 📄diamond_weapon-ex.txt
│  │  │  │  │  ├─ 📄diamond_weapon.ts
│  │  │  │  │  ├─ 📄diamond_weapon.txt
│  │  │  │  │  ├─ 📄emerald_weapon-ex.ts
│  │  │  │  │  ├─ 📄emerald_weapon-ex.txt
│  │  │  │  │  ├─ 📄emerald_weapon.ts
│  │  │  │  │  ├─ 📄emerald_weapon.txt
│  │  │  │  │  ├─ 📄hades-ex.ts
│  │  │  │  │  ├─ 📄hades-ex.txt
│  │  │  │  │  ├─ 📄hades.ts
│  │  │  │  │  ├─ 📄hades.txt
│  │  │  │  │  ├─ 📄innocence-ex.ts
│  │  │  │  │  ├─ 📄innocence-ex.txt
│  │  │  │  │  ├─ 📄innocence.ts
│  │  │  │  │  ├─ 📄innocence.txt
│  │  │  │  │  ├─ 📄levi-un.ts
│  │  │  │  │  ├─ 📄levi-un.txt
│  │  │  │  │  ├─ 📄ruby_weapon-ex.ts
│  │  │  │  │  ├─ 📄ruby_weapon-ex.txt
│  │  │  │  │  ├─ 📄ruby_weapon.ts
│  │  │  │  │  ├─ 📄ruby_weapon.txt
│  │  │  │  │  ├─ 📄shiva-un.ts
│  │  │  │  │  ├─ 📄shiva-un.txt
│  │  │  │  │  ├─ 📄titan-un.ts
│  │  │  │  │  ├─ 📄titan-un.txt
│  │  │  │  │  ├─ 📄titania-ex.ts
│  │  │  │  │  ├─ 📄titania-ex.txt
│  │  │  │  │  ├─ 📄titania.ts
│  │  │  │  │  ├─ 📄titania.txt
│  │  │  │  │  ├─ 📄varis-ex.ts
│  │  │  │  │  ├─ 📄varis-ex.txt
│  │  │  │  │  ├─ 📄wol-ex.ts
│  │  │  │  │  ├─ 📄wol-ex.txt
│  │  │  │  │  ├─ 📄wol.ts
│  │  │  │  │  └─ 📄wol.txt
│  │  │  │  └─ 📁ultimate
│  │  │  │     ├─ 📄the_epic_of_alexander.ts
│  │  │  │     └─ 📄the_epic_of_alexander.txt
│  │  │  ├─ 📁06-ew
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  ├─ 📄aglaia.ts
│  │  │  │  │  ├─ 📄aglaia.txt
│  │  │  │  │  ├─ 📄euphrosyne.ts
│  │  │  │  │  ├─ 📄euphrosyne.txt
│  │  │  │  │  ├─ 📄thaleia.ts
│  │  │  │  │  └─ 📄thaleia.txt
│  │  │  │  ├─ 📁deepdungeon
│  │  │  │  │  ├─ 📄eureka_orthos_floors_01-10.ts
│  │  │  │  │  ├─ 📄eureka_orthos_floors_11-20.ts
│  │  │  │  │  ├─ 📄eureka_orthos_floors_21-30.ts
│  │  │  │  │  ├─ 📄eureka_orthos_floors_31-40.ts
│  │  │  │  │  ├─ 📄eureka_orthos_floors_41-50.ts
│  │  │  │  │  ├─ 📄eureka_orthos_floors_51-60.ts
│  │  │  │  │  ├─ 📄eureka_orthos_floors_61-70.ts
│  │  │  │  │  ├─ 📄eureka_orthos_floors_71-80.ts
│  │  │  │  │  ├─ 📄eureka_orthos_floors_81-90.ts
│  │  │  │  │  ├─ 📄eureka_orthos_floors_91-100.ts
│  │  │  │  │  └─ 📄eureka_orthos_general.ts
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄aetherfont.ts
│  │  │  │  │  ├─ 📄aetherfont.txt
│  │  │  │  │  ├─ 📄aloalo_island.ts
│  │  │  │  │  ├─ 📄aloalo_island.txt
│  │  │  │  │  ├─ 📄alzadaals_legacy.ts
│  │  │  │  │  ├─ 📄alzadaals_legacy.txt
│  │  │  │  │  ├─ 📄another_aloalo_island-savage.ts
│  │  │  │  │  ├─ 📄another_aloalo_island-savage.txt
│  │  │  │  │  ├─ 📄another_aloalo_island.ts
│  │  │  │  │  ├─ 📄another_aloalo_island.txt
│  │  │  │  │  ├─ 📄another_mount_rokkon-savage.ts
│  │  │  │  │  ├─ 📄another_mount_rokkon-savage.txt
│  │  │  │  │  ├─ 📄another_mount_rokkon.ts
│  │  │  │  │  ├─ 📄another_mount_rokkon.txt
│  │  │  │  │  ├─ 📄another_sildihn_subterrane-savage.ts
│  │  │  │  │  ├─ 📄another_sildihn_subterrane-savage.txt
│  │  │  │  │  ├─ 📄another_sildihn_subterrane.ts
│  │  │  │  │  ├─ 📄another_sildihn_subterrane.txt
│  │  │  │  │  ├─ 📄ktisis_hyperboreia.ts
│  │  │  │  │  ├─ 📄ktisis_hyperboreia.txt
│  │  │  │  │  ├─ 📄lapis_manalis.ts
│  │  │  │  │  ├─ 📄lapis_manalis.txt
│  │  │  │  │  ├─ 📄mount_rokkon.ts
│  │  │  │  │  ├─ 📄mount_rokkon.txt
│  │  │  │  │  ├─ 📄smileton.ts
│  │  │  │  │  ├─ 📄smileton.txt
│  │  │  │  │  ├─ 📄stigma_dreamscape.ts
│  │  │  │  │  ├─ 📄stigma_dreamscape.txt
│  │  │  │  │  ├─ 📄the_aitiascope.ts
│  │  │  │  │  ├─ 📄the_aitiascope.txt
│  │  │  │  │  ├─ 📄the_dead_ends.ts
│  │  │  │  │  ├─ 📄the_dead_ends.txt
│  │  │  │  │  ├─ 📄the_fell_court_of_troia.ts
│  │  │  │  │  ├─ 📄the_fell_court_of_troia.txt
│  │  │  │  │  ├─ 📄the_lunar_subterrane.ts
│  │  │  │  │  ├─ 📄the_lunar_subterrane.txt
│  │  │  │  │  ├─ 📄the_sildihn_subterrane.ts
│  │  │  │  │  ├─ 📄the_sildihn_subterrane.txt
│  │  │  │  │  ├─ 📄the_tower_of_babil.ts
│  │  │  │  │  ├─ 📄the_tower_of_babil.txt
│  │  │  │  │  ├─ 📄the_tower_of_zot.ts
│  │  │  │  │  ├─ 📄the_tower_of_zot.txt
│  │  │  │  │  ├─ 📄vanaspati.ts
│  │  │  │  │  └─ 📄vanaspati.txt
│  │  │  │  ├─ 📁hunts
│  │  │  │  │  ├─ 📄elpis.ts
│  │  │  │  │  ├─ 📄garlemald.ts
│  │  │  │  │  ├─ 📄labyrinthos.ts
│  │  │  │  │  ├─ 📄mare_lamentorum.ts
│  │  │  │  │  ├─ 📄ss_rank.ts
│  │  │  │  │  ├─ 📄thavnair.ts
│  │  │  │  │  └─ 📄ultima_thule.ts
│  │  │  │  ├─ 📁map
│  │  │  │  │  ├─ 📄the_excitatron_6000.ts
│  │  │  │  │  └─ 📄the_shifting_gymnasion_agonon.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄p10n.ts
│  │  │  │  │  ├─ 📄p10n.txt
│  │  │  │  │  ├─ 📄p10s.ts
│  │  │  │  │  ├─ 📄p10s.txt
│  │  │  │  │  ├─ 📄p11n.ts
│  │  │  │  │  ├─ 📄p11n.txt
│  │  │  │  │  ├─ 📄p11s.ts
│  │  │  │  │  ├─ 📄p11s.txt
│  │  │  │  │  ├─ 📄p12n.ts
│  │  │  │  │  ├─ 📄p12n.txt
│  │  │  │  │  ├─ 📄p12s.ts
│  │  │  │  │  ├─ 📄p12s.txt
│  │  │  │  │  ├─ 📄p1n.ts
│  │  │  │  │  ├─ 📄p1n.txt
│  │  │  │  │  ├─ 📄p1s.ts
│  │  │  │  │  ├─ 📄p1s.txt
│  │  │  │  │  ├─ 📄p2n.ts
│  │  │  │  │  ├─ 📄p2n.txt
│  │  │  │  │  ├─ 📄p2s.ts
│  │  │  │  │  ├─ 📄p2s.txt
│  │  │  │  │  ├─ 📄p3n.ts
│  │  │  │  │  ├─ 📄p3n.txt
│  │  │  │  │  ├─ 📄p3s.ts
│  │  │  │  │  ├─ 📄p3s.txt
│  │  │  │  │  ├─ 📄p4n.ts
│  │  │  │  │  ├─ 📄p4n.txt
│  │  │  │  │  ├─ 📄p4s.ts
│  │  │  │  │  ├─ 📄p4s.txt
│  │  │  │  │  ├─ 📄p5n.ts
│  │  │  │  │  ├─ 📄p5n.txt
│  │  │  │  │  ├─ 📄p5s.ts
│  │  │  │  │  ├─ 📄p5s.txt
│  │  │  │  │  ├─ 📄p6n.ts
│  │  │  │  │  ├─ 📄p6n.txt
│  │  │  │  │  ├─ 📄p6s.ts
│  │  │  │  │  ├─ 📄p6s.txt
│  │  │  │  │  ├─ 📄p7n.ts
│  │  │  │  │  ├─ 📄p7n.txt
│  │  │  │  │  ├─ 📄p7s.ts
│  │  │  │  │  ├─ 📄p7s.txt
│  │  │  │  │  ├─ 📄p8n.ts
│  │  │  │  │  ├─ 📄p8n.txt
│  │  │  │  │  ├─ 📄p8s.ts
│  │  │  │  │  ├─ 📄p8s.txt
│  │  │  │  │  ├─ 📄p9n.ts
│  │  │  │  │  ├─ 📄p9n.txt
│  │  │  │  │  ├─ 📄p9s.ts
│  │  │  │  │  └─ 📄p9s.txt
│  │  │  │  ├─ 📁trial
│  │  │  │  │  ├─ 📄asura.ts
│  │  │  │  │  ├─ 📄asura.txt
│  │  │  │  │  ├─ 📄barbariccia-ex.ts
│  │  │  │  │  ├─ 📄barbariccia-ex.txt
│  │  │  │  │  ├─ 📄barbariccia.ts
│  │  │  │  │  ├─ 📄barbariccia.txt
│  │  │  │  │  ├─ 📄endsinger-ex.ts
│  │  │  │  │  ├─ 📄endsinger-ex.txt
│  │  │  │  │  ├─ 📄endsinger.ts
│  │  │  │  │  ├─ 📄endsinger.txt
│  │  │  │  │  ├─ 📄golbez-ex.ts
│  │  │  │  │  ├─ 📄golbez-ex.txt
│  │  │  │  │  ├─ 📄golbez.ts
│  │  │  │  │  ├─ 📄golbez.txt
│  │  │  │  │  ├─ 📄hydaelyn-ex.ts
│  │  │  │  │  ├─ 📄hydaelyn-ex.txt
│  │  │  │  │  ├─ 📄hydaelyn.ts
│  │  │  │  │  ├─ 📄hydaelyn.txt
│  │  │  │  │  ├─ 📄rubicante-ex.ts
│  │  │  │  │  ├─ 📄rubicante-ex.txt
│  │  │  │  │  ├─ 📄rubicante.ts
│  │  │  │  │  ├─ 📄rubicante.txt
│  │  │  │  │  ├─ 📄sephirot-un.ts
│  │  │  │  │  ├─ 📄sephirot-un.txt
│  │  │  │  │  ├─ 📄sophia-un.ts
│  │  │  │  │  ├─ 📄sophia-un.txt
│  │  │  │  │  ├─ 📄thordan-un.ts
│  │  │  │  │  ├─ 📄thordan-un.txt
│  │  │  │  │  ├─ 📄ultima-un.ts
│  │  │  │  │  ├─ 📄ultima-un.txt
│  │  │  │  │  ├─ 📄zeromus-ex.ts
│  │  │  │  │  ├─ 📄zeromus-ex.txt
│  │  │  │  │  ├─ 📄zeromus.ts
│  │  │  │  │  ├─ 📄zeromus.txt
│  │  │  │  │  ├─ 📄zodiark-ex.ts
│  │  │  │  │  ├─ 📄zodiark-ex.txt
│  │  │  │  │  ├─ 📄zodiark.ts
│  │  │  │  │  ├─ 📄zodiark.txt
│  │  │  │  │  ├─ 📄zurvan-un.ts
│  │  │  │  │  └─ 📄zurvan-un.txt
│  │  │  │  └─ 📁ultimate
│  │  │  │     ├─ 📄dragonsongs_reprise_ultimate.ts
│  │  │  │     ├─ 📄dragonsongs_reprise_ultimate.txt
│  │  │  │     ├─ 📄the_omega_protocol.ts
│  │  │  │     └─ 📄the_omega_protocol.txt
│  │  │  ├─ 📁07-dt
│  │  │  │  ├─ 📁alliance
│  │  │  │  │  ├─ 📄cloud_of_darkness_chaotic.ts
│  │  │  │  │  ├─ 📄cloud_of_darkness_chaotic.txt
│  │  │  │  │  ├─ 📄jeuno-first-walk.ts
│  │  │  │  │  └─ 📄jeuno-first-walk.txt
│  │  │  │  ├─ 📁dungeon
│  │  │  │  │  ├─ 📄alexandria.ts
│  │  │  │  │  ├─ 📄alexandria.txt
│  │  │  │  │  ├─ 📄ihuykatumu.ts
│  │  │  │  │  ├─ 📄ihuykatumu.txt
│  │  │  │  │  ├─ 📄origenics.ts
│  │  │  │  │  ├─ 📄origenics.txt
│  │  │  │  │  ├─ 📄skydeep-cenote.ts
│  │  │  │  │  ├─ 📄skydeep-cenote.txt
│  │  │  │  │  ├─ 📄strayborough-deadwalk.ts
│  │  │  │  │  ├─ 📄strayborough-deadwalk.txt
│  │  │  │  │  ├─ 📄vanguard.ts
│  │  │  │  │  ├─ 📄vanguard.txt
│  │  │  │  │  ├─ 📄worqor-zormor.ts
│  │  │  │  │  ├─ 📄worqor-zormor.txt
│  │  │  │  │  ├─ 📄yuweyawata.ts
│  │  │  │  │  └─ 📄yuweyawata.txt
│  │  │  │  ├─ 📁hunts
│  │  │  │  │  ├─ 📄heritage_found.ts
│  │  │  │  │  ├─ 📄kozamauka.ts
│  │  │  │  │  ├─ 📄living_memory.ts
│  │  │  │  │  ├─ 📄shaaloani.ts
│  │  │  │  │  ├─ 📄ss_rank.ts
│  │  │  │  │  ├─ 📄urqopacha.ts
│  │  │  │  │  └─ 📄yaktel.ts
│  │  │  │  ├─ 📁map
│  │  │  │  │  └─ 📄cenote_ja_ja_gural.ts
│  │  │  │  ├─ 📁raid
│  │  │  │  │  ├─ 📄r1n.ts
│  │  │  │  │  ├─ 📄r1n.txt
│  │  │  │  │  ├─ 📄r1s.ts
│  │  │  │  │  ├─ 📄r1s.txt
│  │  │  │  │  ├─ 📄r2n.ts
│  │  │  │  │  ├─ 📄r2n.txt
│  │  │  │  │  ├─ 📄r2s.ts
│  │  │  │  │  ├─ 📄r2s.txt
│  │  │  │  │  ├─ 📄r3n.ts
│  │  │  │  │  ├─ 📄r3n.txt
│  │  │  │  │  ├─ 📄r3s.ts
│  │  │  │  │  ├─ 📄r3s.txt
│  │  │  │  │  ├─ 📄r4n.ts
│  │  │  │  │  ├─ 📄r4n.txt
│  │  │  │  │  ├─ 📄r4s.ts
│  │  │  │  │  └─ 📄r4s.txt
│  │  │  │  ├─ 📁trial
│  │  │  │  │  ├─ 📄byakko-un.ts
│  │  │  │  │  ├─ 📄byakko-un.txt
│  │  │  │  │  ├─ 📄queen-eternal-ex.ts
│  │  │  │  │  ├─ 📄queen-eternal-ex.txt
│  │  │  │  │  ├─ 📄queen-eternal.ts
│  │  │  │  │  ├─ 📄queen-eternal.txt
│  │  │  │  │  ├─ 📄valigarmanda-ex.ts
│  │  │  │  │  ├─ 📄valigarmanda-ex.txt
│  │  │  │  │  ├─ 📄valigarmanda.ts
│  │  │  │  │  ├─ 📄valigarmanda.txt
│  │  │  │  │  ├─ 📄zoraal-ja-ex.ts
│  │  │  │  │  ├─ 📄zoraal-ja-ex.txt
│  │  │  │  │  ├─ 📄zoraal-ja.ts
│  │  │  │  │  └─ 📄zoraal-ja.txt
│  │  │  │  └─ 📁ultimate
│  │  │  │     ├─ 📄futures_rewritten.ts
│  │  │  │     └─ 📄futures_rewritten.txt
│  │  │  └─ 📄raidboss_manifest.txt
│  │  ├─ 📁emulator
│  │  │  ├─ 📁data
│  │  │  │  ├─ 📁network_log_converter
│  │  │  │  │  ├─ 📄LineEvent.ts
│  │  │  │  │  ├─ 📄LineEvent0x00.ts
│  │  │  │  │  ├─ 📄LineEvent0x01.ts
│  │  │  │  │  ├─ 📄LineEvent0x02.ts
│  │  │  │  │  ├─ 📄LineEvent0x03.ts
│  │  │  │  │  ├─ 📄LineEvent0x04.ts
│  │  │  │  │  ├─ 📄LineEvent0x0C.ts
│  │  │  │  │  ├─ 📄LineEvent0x105.ts
│  │  │  │  │  ├─ 📄LineEvent0x14.ts
│  │  │  │  │  ├─ 📄LineEvent0x15.ts
│  │  │  │  │  ├─ 📄LineEvent0x16.ts
│  │  │  │  │  ├─ 📄LineEvent0x17.ts
│  │  │  │  │  ├─ 📄LineEvent0x18.ts
│  │  │  │  │  ├─ 📄LineEvent0x19.ts
│  │  │  │  │  ├─ 📄LineEvent0x1A.ts
│  │  │  │  │  ├─ 📄LineEvent0x1B.ts
│  │  │  │  │  ├─ 📄LineEvent0x1C.ts
│  │  │  │  │  ├─ 📄LineEvent0x1D.ts
│  │  │  │  │  ├─ 📄LineEvent0x1E.ts
│  │  │  │  │  ├─ 📄LineEvent0x1F.ts
│  │  │  │  │  ├─ 📄LineEvent0x22.ts
│  │  │  │  │  ├─ 📄LineEvent0x23.ts
│  │  │  │  │  ├─ 📄LineEvent0x24.ts
│  │  │  │  │  ├─ 📄LineEvent0x25.ts
│  │  │  │  │  ├─ 📄LineEvent0x26.ts
│  │  │  │  │  ├─ 📄LineEvent0x27.ts
│  │  │  │  │  ├─ 📄LineEvent0x28.ts
│  │  │  │  │  ├─ 📄LineEvent0x29.ts
│  │  │  │  │  ├─ 📄LineEventBlank.ts
│  │  │  │  │  ├─ 📄LogRepository.ts
│  │  │  │  │  └─ 📄ParseLine.ts
│  │  │  │  ├─ 📄AnalyzedEncounter.ts
│  │  │  │  ├─ 📄Combatant.ts
│  │  │  │  ├─ 📄CombatantJobSearch.ts
│  │  │  │  ├─ 📄CombatantState.ts
│  │  │  │  ├─ 📄CombatantTracker.ts
│  │  │  │  ├─ 📄Encounter.ts
│  │  │  │  ├─ 📄LogEventHandler.ts
│  │  │  │  ├─ 📄NetworkLogConverter.ts
│  │  │  │  ├─ 📄NetworkLogConverter.worker.ts
│  │  │  │  ├─ 📄Persistor.ts
│  │  │  │  ├─ 📄PersistorEncounter.ts
│  │  │  │  ├─ 📄PopupTextAnalysis.ts
│  │  │  │  └─ 📄RaidEmulator.ts
│  │  │  ├─ 📁overrides
│  │  │  │  ├─ 📄RaidEmulatorAnalysisTimelineUI.ts
│  │  │  │  ├─ 📄RaidEmulatorOverlayApiHook.ts
│  │  │  │  ├─ 📄RaidEmulatorPopupText.ts
│  │  │  │  ├─ 📄RaidEmulatorTimeline.ts
│  │  │  │  ├─ 📄RaidEmulatorTimelineController.ts
│  │  │  │  ├─ 📄RaidEmulatorTimelineUI.ts
│  │  │  │  ├─ 📄RaidEmulatorWatchCombatantsOverride.ts
│  │  │  │  └─ 📄StubbedPopupText.ts
│  │  │  ├─ 📁ui
│  │  │  │  ├─ 📄EmulatedPartyInfo.ts
│  │  │  │  ├─ 📄EncounterTab.ts
│  │  │  │  ├─ 📄ProgressBar.ts
│  │  │  │  └─ 📄Tooltip.ts
│  │  │  ├─ 📄EmulatorCommon.ts
│  │  │  ├─ 📄EventBus.ts
│  │  │  ├─ 📄Readme.md
│  │  │  └─ 📄translations.ts
│  │  ├─ 📁skins
│  │  │  ├─ 📁dorgrin
│  │  │  │  ├─ 📄Peace Sans LICENSE.txt
│  │  │  │  ├─ 📄Peace Sans.otf
│  │  │  │  ├─ 📄Readme.md
│  │  │  │  └─ 📄dorgrin.css
│  │  │  ├─ 📁jwidea
│  │  │  │  └─ 📄jwidea.css
│  │  │  └─ 📁lippe
│  │  │     └─ 📄lippe.css
│  │  ├─ 📁test
│  │  │  └─ 📄dragon_test.ts
│  │  ├─ 📄autoplay_helper.ts
│  │  ├─ 📄browser_tts_engine.ts
│  │  ├─ 📄common_replacement.ts
│  │  ├─ 📄html_timeline_ui.ts
│  │  ├─ 📄popup-text.ts
│  │  ├─ 📄raidboss.css
│  │  ├─ 📄raidboss.html
│  │  ├─ 📄raidboss.ts
│  │  ├─ 📄raidboss_alerts_only.html
│  │  ├─ 📄raidboss_config.ts
│  │  ├─ 📄raidboss_options.ts
│  │  ├─ 📄raidboss_silent.html
│  │  ├─ 📄raidboss_timeline_only.html
│  │  ├─ 📄raidemulator.css
│  │  ├─ 📄raidemulator.html
│  │  ├─ 📄raidemulator.ts
│  │  ├─ 📄timeline.ts
│  │  └─ 📄timeline_parser.ts
│  └─ 📁test
│     ├─ 📄test.html
│     ├─ 📄test.ts
│     ├─ 📄timerbar_test.html
│     └─ 📄timerbar_test.ts
├─ 📁user
├─ 📁util
│  ├─ 📁coverage
│  │  ├─ 📄coverage.css
│  │  ├─ 📄coverage.d.ts
│  │  ├─ 📄coverage.html
│  │  ├─ 📄coverage.ts
│  │  ├─ 📄coverage_report.ts
│  │  ├─ 📄missing_translations_cn.html
│  │  ├─ 📄missing_translations_de.html
│  │  ├─ 📄missing_translations_fr.html
│  │  ├─ 📄missing_translations_ja.html
│  │  └─ 📄missing_translations_ko.html
│  ├─ 📁logtools
│  │  ├─ 📄anonymizer.ts
│  │  ├─ 📄arg_parser.ts
│  │  ├─ 📄encounter_printer.ts
│  │  ├─ 📄encounter_tools.ts
│  │  ├─ 📄fake_name_generator.ts
│  │  ├─ 📄fflogs.ts
│  │  ├─ 📄generate_triggers.ts
│  │  ├─ 📄make_timeline.ts
│  │  ├─ 📄notifier.ts
│  │  ├─ 📄split_log.ts
│  │  ├─ 📄splitter.css
│  │  ├─ 📄splitter.html
│  │  ├─ 📄splitter.ts
│  │  ├─ 📄test_timeline.ts
│  │  └─ 📄web_splitter.ts
│  ├─ 📄DEPS.json5
│  ├─ 📄README.md
│  ├─ 📄bump_version.ts
│  ├─ 📄console_logger.ts
│  ├─ 📄csv_util.ts
│  ├─ 📄do_release.ts
│  ├─ 📄example_log_lines.ts
│  ├─ 📄fetch_deps.ts
│  ├─ 📄file_utils.ts
│  ├─ 📄find_missing_timeline_translations.ts
│  ├─ 📄find_missing_translations.ts
│  ├─ 📄find_missing_translations_action.ts
│  ├─ 📄gen_coverage_report.ts
│  ├─ 📄gen_effect_id.ts
│  ├─ 📄gen_hunt_data.ts
│  ├─ 📄gen_log_guide.ts
│  ├─ 📄gen_pet_names.ts
│  ├─ 📄gen_weather_rate.ts
│  ├─ 📄gen_world_ids.ts
│  ├─ 📄gen_zone_id_and_info.ts
│  ├─ 📄generate_data_files.ts
│  ├─ 📄index.ts
│  ├─ 📄manifest.ts
│  ├─ 📄process_triggers_folder.ts
│  ├─ 📄publish.sh
│  ├─ 📄query_xivapi.ts
│  ├─ 📄sync_files.ts
│  ├─ 📄translate_timeline.ts
│  ├─ 📄update_logdefs.ts
│  ├─ 📄validate_versions.ts
│  ├─ 📄xivapi.ts
│  └─ 📄zone_overrides.ts
├─ 📁webpack
│  ├─ 📁loaders
│  │  └─ 📄manifest-loader.ts
│  ├─ 📄constants.ts
│  ├─ 📄webpack.config.ts
│  ├─ 📄webpack.dev.ts
│  ├─ 📄webpack.ghpages.ts
│  └─ 📄webpack.prod.ts
├─ 📄.editorconfig
├─ 📄.eslintrc.cjs
├─ 📄.gitattributes
├─ 📄.gitignore
├─ 📄.markdownlint.yml
├─ 📄.mocharc.cjs
├─ 📄.npmrc
├─ 📄.yamllint.yml
├─ 📄CODE_OF_CONDUCT.md
├─ 📄CONTRIBUTING.md
├─ 📄LICENSE
├─ 📄README.md
├─ 📄dprint.json
├─ 📄package-lock.json
├─ 📄package.json
├─ 📄requirements.txt
├─ 📄tsconfig.eslint.json
├─ 📄tsconfig.json
├─ 📄tsconfig.node.json
├─ 📄tsconfig.npm.json
└─ 📄tsconfig.webpack.json
```