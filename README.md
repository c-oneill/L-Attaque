<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]


<!-- PROJECT LOGO -->
<br />
<p align="center">
    <!--
    <a href="https://github.com/c-oneill/L-Attaque">
        <img src="images/logo.png" alt="Logo" width="80" height="80">
    </a>
    -->
    <h1 align="center">Stratego</h1>
    <p align="center">
        A virtual adaption of Stratego, the two-player strategy board game based of the
        early 20th century French board game, L'Attaque. Stratego might be described as
        a mashup of capture the flag, chess, and battleship.
        <br />
        <a href="https://github.com/c-oneill/L-Attaque/doc/index.html">Explore the Docs</a>
        ·
        <a href="https://github.com/c-oneill/L-Attaque/issues">Report Bug</a>
        ·
        <a href="https://github.com/c-oneill/L-Attaque/issues">Request Feature</a>
    </p>
</p>

<!-- TABLE OF CONTENTS -->
<details open="open">
    <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
    <ol>
        <li>
            <a href="#about-the-project">About The Project</a>
            <ul>
                <li><a href="#built-with">Built With</a></li>
            </ul>
        </li>
        <li>
            <a href="#getting-started">Getting Started</a>
            <ul>
                <li><a href="#run-with-eclipse">Run with Eclipse</a></li>
                <li><a href="#run-with-maven">Run with Maven</a></li>
            </ul>
        </li>
        <li>
            <a href="#usage">Usage</a>
            <ul>
                <li><a href="#rules">Rules</a></li>
                <li><a href="#establishing-a-connection">Establishing a Connection</a></li>
                <li><a href="#setup-board">Setup Board</a></li>
                <li><a href="#battle">Battle</a></li>
                <li><a href="#chat">Chat</a></li>
            </ul>
        </li>
        <li><a href="#roadmap">Roadmap</a></li>
        <li><a href="#contributing">Contributing</a></li>
        <li><a href="#license">License</a></li>
        <li><a href="#contact">Contact</a></li>
        <li><a href="#acknowledgements">Acknowledgements</a></li>
    </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This is a virtual adaption of the strategy board game [Stratego](https://en.wikipedia.org/wiki/Stratego).

![Stratego mid-game screenshot](/images/chat.png)


### Built With

* [Maven](https://maven.apache.org/) - Dependency Management



<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

<!-- ### Prerequisites -->

#### Run with Eclipse

1. Clone the repo
    ```sh
    git clone https://github.com/c-oneill/L-Attaque.git
    ```
2. Import git project in Eclipse
    ```
    File → Import → Git → Projects from Git
    ```
3. Select cloned project
    ```
    Existing local repository → add... → select base directory
    ```
4. Start an instance
    * Right-click project in Package Explorer
    * Click 'Run as...' and select 'Java Application'

* To run the tests in Eclipse:
    * Right-click project in Package Explorer
    * Click 'Run as...' and select 'JUNIT Test'

#### Run with Maven

1. Clone the repo
    ```sh
    git clone https://github.com/c-oneill/L-Attaque.git
    ```
2. In the base project directory:
    ```sh
    mvn clean package
    ```
3. Start an instance
    ```sh
    java -jar bin/Stratego-0.0.1-SNAPSHOT-jar-with-dependencies.jar
    ```

* To run the tests with Maven:
    ```sh
    mvn clean test
    ```


<!-- USAGE EXAMPLES -->
## Usage

#### Rules

This virtual adaption follows the rules of classic Stratego. 
The [Stratego wikipedia](https://en.wikipedia.org/wiki/Stratego#Setup) entry has good summary of the rules.

#### Establishing a Connection

* There is no feature (yet) for single-player games (an automated opponent), so it's necessary to establish 
two instances - locally or over a network.
* To start a game:
    1. Start the server
        * Select `Server`
        * `File → New Game → OK`
    2. Start the Client
        * Select `Client`
        * `File → New Game → OK`
* Attempting to open a new game as client *before* the server game is started will fail and prompt a warning.
* Board and pieces box locked until connected.

![Establishing a connection screenshot](/images/connection.png)

#### Setup Board 
* Drag Pieces from the box on the left to the bottom 4 x 10 portion of the board.
* Two minutes allowed to setup from the time the connection is established.
* Setup ends with `File → Setup Done` or the timer runs out.
* Unfilled slots are filled arbitrarily (not randomly though).
* The screen locked and game doesn't begin until you and your opponent are setup.

![Board setup](/images/setup.png)

#### Battle
* The battle portion is turn-based with server instance going first.
* The games ends under three scenarios:
    * One user decides to end the game:
    `File → End Game`
    * Either flag is captured.
    * One player loses all moveable pieces.

![Mid battle](/images/midgame.png)

#### Chat 

* Once connection is established, can chat with the opponent in real time.
* Text is tagged with player color.
* Chat Connection with opponent is preserved after games ends (Feature or a bug? This will likely be removed)
* Can cutomize the chat color under `Options`.

![Chat options](/images/chatCustomization.png)



<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/c-oneill/L-Attaque/issues) for a list of proposed features (and known issues).



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Caroline O'Neill - <csoneill@email.arizona.edu>

Project Link: <https://github.com/c-oneill/L-Attaque>



<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements

* main contributor: [Caroline O'Neill](https://github.com/c-oneill)
* main contributor: [Kristopher Rangel](https://github.com/b-glitch)
* test classes: [jszs2009](https://github.com/jszs2009)

* README format inpired by this [template](https://github.com/othneildrew/Best-README-Template) 



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo.svg?style=for-the-badge
[contributors-url]: https://github.com/c-oneill/L-Attaque/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo.svg?style=for-the-badge
[forks-url]: https://github.com/c-oneill/L-Attaque/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo.svg?style=for-the-badge
[stars-url]: https://github.com/c-oneill/L-Attaque/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo.svg?style=for-the-badge
[issues-url]: https://github.com/c-oneill/L-Attaque/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo.svg?style=for-the-badge
[license-url]: https://github.com/c-oneill/L-Attaque/blob/master/LICENSE.txt